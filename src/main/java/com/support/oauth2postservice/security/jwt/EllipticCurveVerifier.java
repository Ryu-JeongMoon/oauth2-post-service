package com.support.oauth2postservice.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class EllipticCurveVerifier implements TokenVerifier {

  private final Ed25519Verifier ed25519Verifier;

  @Override
  public boolean isValid(String token) {
    SignedJWT signedJWT = parse(token);
    try {
      return signedJWT.verify(ed25519Verifier);
    } catch (JOSEException e) {
      return false;
    }
  }

  private SignedJWT parse(String token) {
    try {
      return SignedJWT.parse(token);
    } catch (ParseException e) {
      log.info("JWT PARSING ERROR => {}", e.getMessage());
      throw new TokenException(ExceptionMessages.Token.WRONG_FORMAT);
    }
  }

  @SneakyThrows(value = ParseException.class)
  public Authentication getAuthentication(String accessToken) {
    JWTClaimsSet claimsSet = parse(accessToken).getJWTClaimsSet();
    String id = claimsSet.getStringClaim(TokenConstants.USER_ID);
    String email = claimsSet.getSubject();
    List<SimpleGrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(claimsSet.getStringClaim(TokenConstants.AUTHORITIES)));

    return UserPrincipal
        .of(id, email, authorities)
        .toAuthentication();
  }
}
