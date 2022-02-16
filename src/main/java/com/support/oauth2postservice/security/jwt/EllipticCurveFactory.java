package com.support.oauth2postservice.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.support.oauth2postservice.util.constant.Times;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EllipticCurveFactory extends TokenFactory {

  private final OctetKeyPair privateJsonWebKey;
  private final OctetKeyPair publicJsonWebKey;
  private final Ed25519Signer ed25519Signer;

  public EllipticCurveFactory() throws JOSEException {
    privateJsonWebKey = new OctetKeyPairGenerator(Curve.Ed25519)
        .keyUse(KeyUse.SIGNATURE)
        .keyID(UUID.randomUUID().toString())
        .generate();
    ed25519Signer = new Ed25519Signer(privateJsonWebKey);

    publicJsonWebKey = privateJsonWebKey.toPublicJWK();
  }

  @Bean
  public Ed25519Verifier ed25519Verifier() throws JOSEException {
    return new Ed25519Verifier(publicJsonWebKey);
  }

  @Override
  protected String createAccessToken(Authentication authentication) {
    long currentTime = new Date().getTime();

    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(authentication.getName())
        .claim(TokenConstants.AUTHORITIES, authorities)
        .claim(TokenConstants.TYPE, TokenConstants.ACCESS_ONLY)
        .expirationTime(new Date(currentTime + Times.ACCESS_TOKEN_EXPIRATION_MILLIS.getNumber()))
        .build();

    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.EdDSA)
        .keyID(privateJsonWebKey.getKeyID())
        .build();

    return createNewJWT(header, claimsSet);
  }

  @Override
  protected String createRefreshToken(Authentication authentication) {
    long currentTime = new Date().getTime();

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(authentication.getName())
        .claim(TokenConstants.TYPE, TokenConstants.REFRESH_ONLY)
        .expirationTime(new Date(currentTime + Times.REFRESH_TOKEN_EXPIRATION_MILLIS.getNumber()))
        .build();

    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.EdDSA)
        .keyID(privateJsonWebKey.getKeyID())
        .build();

    return createNewJWT(header, claimsSet);
  }

  private String createNewJWT(JWSHeader header, JWTClaimsSet claimsSet) {
    SignedJWT jwtToBeSerialized = new SignedJWT(header, claimsSet);
    try {
      jwtToBeSerialized.sign(ed25519Signer);
    } catch (JOSEException e) {
      log.info("JWT SIGN ERROR => {}", e.getMessage());
      throw new TokenException(ExceptionMessages.NOT_SIGNED_TOKEN);
    }
    return jwtToBeSerialized.serialize();
  }
}
