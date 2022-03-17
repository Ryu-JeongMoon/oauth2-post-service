package com.support.oauth2postservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.security.oauth2.OAuth2Attributes;
import com.support.oauth2postservice.security.service.CustomOAuth2MemberService;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GoogleOidcVerifier implements OAuth2TokenVerifier {

  private final WebClientWrappable webClientWrappable;
  private final CustomOAuth2MemberService customOAuth2MemberService;

  @Override
  public boolean isValid(String token) {
    return webClientWrappable.validateByOidc(token);
  }

  @Override
  public Authentication getAuthentication(String idToken) {
    DecodedJWT decodedJWT = parse(idToken);

    Map<String, Claim> claims = decodedJWT.getClaims();
    Map<String, Object> attributes = claims
        .keySet()
        .stream()
        .collect(Collectors.toMap(key -> key, key -> claims.get(key).as(Object.class)));

    OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(AuthProvider.GOOGLE.name(), attributes);
    Member member = customOAuth2MemberService.getMember(AuthProvider.GOOGLE.name(), oAuth2Attributes);
    return OAuth2UserPrincipal
        .from(member)
        .toAuthentication();
  }

  private DecodedJWT parse(String idToken) throws JWTDecodeException {
    return JWT.decode(idToken);
  }

  @Override
  public boolean isGoogleToken(String idToken) {
    try {
      DecodedJWT decodedJWT = parse(idToken);
      return StringUtils.equalsIgnoreCase(TokenConstants.GOOGLE_TOKEN_ISSUER, decodedJWT.getIssuer());
    } catch (JWTDecodeException e) {
      return false;
    }
  }
}
