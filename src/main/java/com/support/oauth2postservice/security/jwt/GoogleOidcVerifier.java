package com.support.oauth2postservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOidcVerifier implements OAuth2TokenVerifier {

  private final MemberRepository memberRepository;
  private final WebClientWrappable webClientWrappable;

  @Override
  public boolean isValid(String token) {
    return webClientWrappable.validateByOidc(token);
  }

  @Override
  public Authentication getAuthentication(String idToken) {
    DecodedJWT decodedJWT = parse(idToken);
    String email = decodedJWT.getClaim(TokenConstants.EMAIL).asString();
    Member member = memberRepository.findActiveByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    return OAuth2UserPrincipal
        .from(member)
        .toAuthentication();
  }

  private DecodedJWT parse(String idToken) {
    try {
      return JWT.decode(idToken);
    } catch (JWTDecodeException e) {
      throw new TokenException(ExceptionMessages.Token.WRONG_FORMAT, e);
    }
  }

  @Override
  public boolean isGoogleToken(String idToken) {
    try {
      DecodedJWT decodedJWT = parse(idToken);
      return StringUtils.equalsIgnoreCase(TokenConstants.OAUTH2_GOOGLE_TOKEN_ISSUER, decodedJWT.getIssuer());
    } catch (TokenException e) {
      return false;
    }
  }
}
