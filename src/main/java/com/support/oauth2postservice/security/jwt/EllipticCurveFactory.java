package com.support.oauth2postservice.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.util.constant.Times;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * The 2021 TLS Telemetry Report 보고서에 따르면 많은 웹사이트가 점점 TLS v1.3으로 옮겨가는 추세이고<br/>
 * ECC 256 bits 의 안정성은 RSA 3072 bits 와 동일하다<br/>
 * 따라서 RSA 와 비교하여 안정성은 더 높고 리소스 사용률이 적은 ECC 를 채택한다<br/><br/>
 * 2016년 기준 ECC 암호화 알고리즘에서 가장 많이 사용된 P-256 알고리즘의 경우<br/>
 * ECC 알고리즘에서 보안을 위해 요구되는 요소 중 아래 요소들이 충족되지 않았다<br/>
 * rigidity, ladders, completeness, indistinguishability <br/>
 * 따라서 JWT 생성과 검증에는 P-256 대신 보안 요구 사항이 충족된 Ed25519 알고리즘을 사용한다<br/>
 */
@Slf4j
@Component
@EnableConfigurationProperties(EllipticCurveProperties.class)
public class EllipticCurveFactory extends TokenFactory {

  private final OctetKeyPair privateJsonWebKey;
  private final OctetKeyPair publicJsonWebKey;
  private final Ed25519Signer ed25519Signer;

  public EllipticCurveFactory(EllipticCurveProperties ellipticCurveProperties) throws JOSEException {
    privateJsonWebKey = new OctetKeyPairGenerator(Curve.Ed25519)
        .keyUse(KeyUse.SIGNATURE)
        .keyID(ellipticCurveProperties.getSecretKey())
        .generate();
    ed25519Signer = new Ed25519Signer(privateJsonWebKey);

    publicJsonWebKey = privateJsonWebKey.toPublicJWK();
  }

  @Bean
  public Ed25519Verifier ed25519Verifier() throws JOSEException {
    return new Ed25519Verifier(publicJsonWebKey);
  }

  @Override
  protected String createIdToken(Authentication authentication) {
    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.EdDSA)
        .keyID(privateJsonWebKey.getKeyID())
        .build();

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(authentication.getName())
        .issuer(TokenConstants.LOCAL_TOKEN_ISSUER)
        .claim(TokenConstants.AUTHORITIES, getAuthorities(authentication))
        .claim(TokenConstants.TOKEN_TYPE, TokenConstants.ID_TOKEN)
        .claim(TokenConstants.USER_ID, getIdFromAuthentication(authentication))
        .expirationTime(createExpirationDate(Times.ACCESS_TOKEN_EXPIRATION_SECONDS))
        .build();

    return createNewJWT(header, claimsSet);
  }

  private String getAuthorities(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(grantedAuthority -> grantedAuthority.getAuthority().replace(TokenConstants.ROLE_PREFIX, ""))
        .findAny()
        .orElseGet(Role.USER::name);
  }

  private String getIdFromAuthentication(Authentication authentication) {
    Object principal = authentication.getPrincipal();
    if (!(principal instanceof UserPrincipal))
      return "";

    return ((UserPrincipal) principal).getId();
  }

  @Override
  protected String createAccessToken(Authentication authentication) {
    try {
      String ellipticCurvePoint = new ECKeyGenerator(Curve.P_256)
          .keyUse(KeyUse.ENCRYPTION)
          .generate()
          .getX()
          .toString();

      return TokenConstants.ACCESS_TOKEN_PREFIX + ellipticCurvePoint;
    } catch (JOSEException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  protected String createRefreshToken(Authentication authentication) {
    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.EdDSA)
        .build();

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(authentication.getName())
        .claim(TokenConstants.AUTHORITIES, getAuthorities(authentication))
        .claim(TokenConstants.USER_ID, getIdFromAuthentication(authentication))
        .expirationTime(createExpirationDate(Times.REFRESH_TOKEN_EXPIRATION_SECONDS))
        .build();

    return createNewJWT(header, claimsSet);
  }

  private String createNewJWT(JWSHeader header, JWTClaimsSet claimsSet) {
    SignedJWT jwtToBeSerialized = new SignedJWT(header, claimsSet);
    try {
      jwtToBeSerialized.sign(ed25519Signer);
    } catch (JOSEException e) {
      log.info("JWT SIGN ERROR => {}", e.getMessage());
      throw new TokenException(ExceptionMessages.Token.NOT_SIGNED);
    }
    return jwtToBeSerialized.serialize();
  }
}
