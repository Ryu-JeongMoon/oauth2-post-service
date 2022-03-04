package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MockWebClientWrapper implements WebClientWrappable {

  private static final String SCOPE = "panda";
  private static final String TOKEN_TYPE = "bear";
  private static final String EXPIRES_IN = "rabbit";
  private static final String ACCESS_TOKEN = "mouse";
  private static final String REFRESH_TOKEN = "tiger";
  private static final String OIDC_ID_TOKEN = "elephant";

  @Override
  public boolean validateByOidc(String idToken) {
    return StringUtils.hasText(idToken);
  }

  /**
   * query-string 으로 받아오는 code 또는 refresh_token<br/>
   * 있을 시엔 값이 있는 객체를 반환하고 없다면 값들이 null 로 채워진 객체를 반환한다<br/>
   * 실제 사용되는 WebClientWrapper 의 로직과 동일한 응답값을 내리기 위한 형태이고<br/>
   * 추후 비지니스 로직 수정에 따라 메서드의 로직 또한 수정 되어야 한다
   */
  @Override
  public OAuth2TokenResponse getToken(OAuth2TokenRequest oAuth2TokenRequest) {
    if (StringUtils.hasText(oAuth2TokenRequest.getCode()) || StringUtils.hasText(oAuth2TokenRequest.getRefreshToken()))
      return new OAuth2TokenResponse(SCOPE, TOKEN_TYPE, EXPIRES_IN, ACCESS_TOKEN, REFRESH_TOKEN, OIDC_ID_TOKEN);

    return OAuth2TokenResponse.empty();
  }

  @Override
  public OAuth2TokenResponse getRenewedToken(OAuth2TokenRequest oAuth2TokenRequest) {
    return getToken(oAuth2TokenRequest);
  }
}
