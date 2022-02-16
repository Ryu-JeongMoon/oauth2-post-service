package com.support.oauth2postservice.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jwt.SignedJWT;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Slf4j
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
      log.info("JWT VERIFYING ERROR => {}", e.getMessage());
      throw new TokenException(ExceptionMessages.NOT_VERIFIED_TOKEN);
    }
  }

  private SignedJWT parse(String token) {
    try {
      return SignedJWT.parse(token);
    } catch (ParseException e) {
      log.info("JWT PARSING ERROR => {}", e.getMessage());
      throw new TokenException(ExceptionMessages.WRONG_FORMAT_TOKEN);
    }
  }
}
