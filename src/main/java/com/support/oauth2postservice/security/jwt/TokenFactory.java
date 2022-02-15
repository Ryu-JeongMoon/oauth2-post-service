package com.support.oauth2postservice.security.jwt;

import org.springframework.security.core.Authentication;

public abstract class TokenFactory {

    public TokenResponse create(Authentication authentication) {
        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken(authentication);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    protected abstract String createAccessToken(Authentication authentication);

    protected abstract String createRefreshToken(Authentication authentication);
}
