package com.support.oauth2postservice.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class EllipticCurveVerifier implements TokenVerifier {

  private final Ed25519Verifier ed25519Verifier;

  @Override
  public boolean isValid(String token) {
    try {
      SignedJWT signedJWT = parse(token);
      return signedJWT.verify(ed25519Verifier);
    } catch (JOSEException | ParseException e) {
      return false;
    }
  }

  @Override
  public boolean isLocalToken(String accessToken) {
    try {
      SignedJWT signedJWT = parse(accessToken);
      String issuer = signedJWT.getJWTClaimsSet().getIssuer();
      return StringUtils.equalsIgnoreCase(TokenConstants.LOCAL_TOKEN_ISSUER, issuer);
    } catch (ParseException e) {
      return false;
    }
  }

  private SignedJWT parse(String token) throws ParseException {
    return SignedJWT.parse(token);
  }

  @SneakyThrows(value = ParseException.class)
  public Authentication getAuthentication(String accessToken) {
    JWTClaimsSet claimsSet = parse(accessToken).getJWTClaimsSet();

    String email = claimsSet.getSubject();
    String id = claimsSet.getStringClaim(TokenConstants.USER_ID);
    String authority = claimsSet.getStringClaim(TokenConstants.AUTHORITIES);
    List<Role> authorities = Collections.singletonList(Role.caseInsensitiveValueOf(authority));

    return UserPrincipal
        .of(id, email, authorities)
        .toAuthentication();
  }
}
