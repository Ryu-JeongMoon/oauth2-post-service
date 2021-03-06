package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.security.jwt.TokenResponse;
import com.support.oauth2postservice.security.jwt.TokenVerifier;
import com.support.oauth2postservice.service.LocalTokenService;
import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class AuthViewController {

  private final TokenVerifier tokenVerifier;
  private final LocalTokenService localTokenService;

  @GetMapping(UriConstants.Mapping.LOGIN)
  @PreAuthorize(SpELConstants.ANONYMOUS_ONLY)
  public String loginPage(HttpServletRequest request, Model model) {
    model.addAttribute("hasRefreshToken", hasRefreshToken(request));
    return "login";
  }

  /**
   * 구글 로그인의 경우 두 가지 방식이 존재하고 로그인 URL이 다르다<br/>
   * 1. 리프레시 토큰 없이 액세스 토큰만 발급 받는 방식<br/>
   * 2. 리프레시 토큰과 액세스 토큰을 함께 발급 받는 방식<br/><br/>
   * 1년의 유효 기간을 갖는 access_token 이름의 쿠키를 발급하고<br/>
   * 쿠키가 없는 경우에 한해서 리프레시 토큰까지 함께 가져오는 로그인 방식으로 유도한다<br/>
   * 로그인 한 적이 없는 경우 리프레시 토큰이 존재하지 않고<br/>
   * 로그인 한 적이 있더라도 1년의 기간이 지난 후엔 보안을 위해 리프레시 토큰까지 요청한다<br/><br/>
   * 따라서 이 메서드에서 리프레시 토큰의 존재 여부를 access_token 쿠키의 존재 여부로 확인한다<br/>
   */
  private boolean hasRefreshToken(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.ACCESS_TOKEN)
        .isPresent();
  }

  @GetMapping(UriConstants.Mapping.RENEW_LOCAL_TOKEN_AND_REDIRECT)
  public String renewAndRedirect(
      @RequestParam(value = TokenConstants.REDIRECT_URI, defaultValue = UriConstants.Mapping.ROOT) String redirectUri,
      @RequestParam(value = TokenConstants.REFRESH_TOKEN) String refreshToken,
      HttpServletResponse response) {

    TokenResponse renewedTokenResponse = localTokenService.renew(refreshToken);

    String idToken = renewedTokenResponse.getIdToken();
    Authentication authentication = tokenVerifier.getAuthentication(idToken);
    SecurityUtils.setAuthentication(authentication);

    CookieUtils.addLocalTokenToBrowser(response, renewedTokenResponse);
    return UriConstants.Keyword.REDIRECT + redirectUri;
  }

  @GetMapping(UriConstants.Mapping.LOGOUT)
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    SecurityContextHolder.clearContext();
    CookieUtils.deleteCookie(request, response, TokenConstants.ID_TOKEN);
    return UriConstants.Keyword.REDIRECT + UriConstants.Mapping.ROOT;
  }
}
