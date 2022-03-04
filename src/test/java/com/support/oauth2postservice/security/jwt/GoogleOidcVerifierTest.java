package com.support.oauth2postservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;

class GoogleOidcVerifierTest {

  private final String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjNkZDZjYTJhODFkYzJmZWE4YzM2NDI0MzFlN2UyOTZkMmQ3NWI0NDYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0NDc2Njg1MDI2OTUtaWxkMXJtNXNiaXZiOWlua3J2dWlkZDg5YWdwbWMyNmcuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0NDc2Njg1MDI2OTUtaWxkMXJtNXNiaXZiOWlua3J2dWlkZDg5YWdwbWMyNmcuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDIwOTU0NjY5NzYxNjQyNzM2OTYiLCJlbWFpbCI6InJqbTkzMDNAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJ5T2VkLUFSV01iV1BqOFVpUWhNamVBIiwibmFtZSI6Ik1vb24iLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EtL0FPaDE0R2hkQlIySm5zeTNUc3JkVTQybFRHYS1nOXhUQ0xITzRiZ3hLVlZ4PXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6Ik1vb24iLCJsb2NhbGUiOiJrbyIsImlhdCI6MTY0NjM1NzIyOCwiZXhwIjoxNjQ2MzYwODI4fQ.TeHKZn7GcrDXvckir1-8GzoTPHr5BJXVYpKn-T50IwvRlaZOSagL2Yo1s8CptcaGRLY_GVOLAbzxWhViu1oD-uNDXoY6nWoEJ-ksFDXtR-JS7zz0mP3AHCaoIxUzq5_3VvB51LQdvx1E0cRv5IEeZO2rOPv_HUaXfxkKiI1M-VQTSKPOt8hfaRWS36IChg3HvgBhqrCksOO-Wpzh1CEHWR9BwB9WM9DZA77888uoN0EPGuQ_XLiDdSaiWrBRXYb6FVJo4zpvbMHbc_zdt-kEZLigHiGnck7D4MeWbpu9Z7JRDWk0eT_pvE9mAP-6mHB-vgXKoPIlSt7TRA4e_UmPeg";
  private final String accessToken = "ya29.A0ARrdaM_uBASQPOnRA9KE3_u9uADkL2HGwcKE0dsv_z6TbVKeStpzsUbckGRWniZxvVcU5wfT843Z2esWfMRatT1uGPnj9cqtvNAjCjerxlYALBMB_Ifgc-anHbzArR44zNnqU6VcB4pwfyWD4BoQCREdEy8p";

  @Test
  @DisplayName("URL Decoder 활용")
  void decodeByString() {

    String[] chunks = idToken.split("\\.");
    Base64.Decoder urlDecoder = Base64.getUrlDecoder();

    String header = new String(urlDecoder.decode(chunks[0]));
    String payload = new String(urlDecoder.decode(chunks[1]));

    System.out.println("header = " + header);
    System.out.println("payload = " + payload);

    Assertions.assertThat(header).isNotBlank();
    Assertions.assertThat(payload).isNotBlank();
  }

  @Test
  @DisplayName("java-jwt 라이브러리 활용")
  void decodeByJwtLibrary() {
    DecodedJWT jwt = JWT.decode(idToken);

    String iss = jwt.getClaim("iss").asString();
    String azp = jwt.getClaim("azp").asString();
    String aud = jwt.getClaim("aud").asString();
    String sub = jwt.getClaim("sub").asString();
    String email = jwt.getClaim("email").asString();
    String atHash = jwt.getClaim("at_hash").asString();
    String name = jwt.getClaim("name").asString();
    String picture = jwt.getClaim("picture").asString();
    String givenName = jwt.getClaim("given_name").asString();
    String locale = jwt.getClaim("locale").asString();
    Date iat = jwt.getClaim("iat").asDate();
    Date exp = jwt.getClaim("exp").asDate();
    boolean emailVerified = jwt.getClaim("email_verified").asBoolean();
  }
}