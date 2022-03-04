package com.support.oauth2postservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

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

    return null;
  }

  private DecodedJWT parse(String idToken) {
    try {
      return JWT.decode(idToken);
    } catch (JWTDecodeException e) {
      throw new TokenException(ExceptionMessages.Token.WRONG_FORMAT, e);
    }
  }
}
