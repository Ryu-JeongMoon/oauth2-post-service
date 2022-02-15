package com.support.oauth2postservice.security.jwt;

public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
}
