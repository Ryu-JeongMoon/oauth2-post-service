package com.support.oauth2postservice.security.jwt.factory;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;
import com.nimbusds.jose.util.Base64URL;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.jwt.EllipticCurveFactory;
import com.support.oauth2postservice.security.jwt.EllipticCurveVerifier;
import com.support.oauth2postservice.security.jwt.TokenResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EC 알고리즘 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {EllipticCurveFactory.class, EllipticCurveVerifier.class})
class EllipticCurveFactoryTest {

  @Autowired
  EllipticCurveFactory ellipticCurveFactory;
  @Autowired
  EllipticCurveVerifier ellipticCurveVerifier;

  private TokenResponse tokenResponse;

  @BeforeAll
  void setUp() {
    Member member = MemberTestHelper.createUser();
    UserPrincipal principal = UserPrincipal.from(member);

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        principal, "",
        Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString())));

    tokenResponse = ellipticCurveFactory.create(authenticationToken);
  }

  @Test
  @DisplayName("키 생성 확인")
  void checkKeyGeneration() {
    assertThat(tokenResponse).isNotNull();
  }

  @Nested
  @DisplayName("키 검증 확인")
  class EllipticCurveVerifierTest {

    @Test
    @DisplayName("ID Token 확인 성공")
    void isValid_idToken() {
      boolean validity = ellipticCurveVerifier.isValid(tokenResponse.getIdToken());
      assertTrue(validity);
    }

    @Test
    @DisplayName("ID Token 확인 실패")
    void isValid_idToken_failByWrongToken() {
      boolean validity = ellipticCurveVerifier.isValid(tokenResponse.getIdToken() + 1);
      assertFalse(validity);
    }

    @Test
    @DisplayName("Refresh Token 확인")
    void isValid_refreshToken() {
      boolean validity = ellipticCurveVerifier.isValid(tokenResponse.getRefreshToken());
      assertTrue(validity);
    }

    @Test
    @DisplayName("Refresh Token 확인 실패")
    void isValid_refreshToken_failByWrongToken() {
      boolean validity = ellipticCurveVerifier.isValid(tokenResponse.getRefreshToken() + 1);
      assertFalse(validity);
    }

    @Test
    @DisplayName("Token 확인 false - 빈 토큰 값")
    void isValid_token_failByEmptyToken() {
      boolean validity = ellipticCurveVerifier.isValid("");
      assertFalse(validity);
    }
  }

  @Test
  @DisplayName("JWS 예제")
  void generateJWSExample() throws JOSEException {
    ECKey ecJWK = new ECKeyGenerator(Curve.P_256)
        .keyID("123")
        .generate();
    ECKey ecPublicJWK = ecJWK.toPublicJWK();

    // Create the EC signer
    JWSSigner signer = new ECDSASigner(ecJWK);

    // Creates the JWS object with payload
    JWSObject jwsObject = new JWSObject(
        new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(ecJWK.getKeyID()).build(),
        new Payload("Elliptic cure"));

    // Compute the EC signature
    jwsObject.sign(signer);

    // Serialize the JWS to compact form
    String jwsObjectString = jwsObject.serialize();

    // The recipient creates a verifier with the public EC key
    JWSVerifier verifier = new ECDSAVerifier(ecPublicJWK);

    System.out.println("jwsObjectString = " + jwsObjectString);
  }

  @Test
  @DisplayName("JWE 예제")
  void generateJWEExample() throws JOSEException {
    ECKey ecJWK = new ECKeyGenerator(Curve.P_521)
        .keyID("123")
        .generate();
    ECKey ecPublicJWK = ecJWK.toPublicJWK();

    ECDHEncrypter ecdhEncrypter = new ECDHEncrypter(ecPublicJWK);

    JWEObject jweObject = new JWEObject(
        new JWEHeader.Builder(JWEAlgorithm.ECDH_ES_A256KW, EncryptionMethod.A256GCM).build(),
        new Payload("Elliptic cure")
    );
    jweObject.encrypt(ecdhEncrypter);

    String jweObjectString = jweObject.serialize();

    assertNotNull(jweObjectString);
  }

  @Test
  @DisplayName("EC KEY - 랜덤 문자열 생성")
  void randomStringFromECKey() throws JOSEException {
    ECKey ecJWK = new ECKeyGenerator(Curve.P_521)
        .keyID("123")
        .generate();

    System.out.println("ecJWK.toString() = " + ecJWK.toString());
    System.out.println("ecJWK.getD() = " + ecJWK.getD());
    System.out.println("ecJWK.getX() = " + ecJWK.getX());

    assertNotNull(ecJWK.getD());
    assertNotNull(ecJWK.getX());
  }

  @Test
  @DisplayName("OctetKeyPair - 랜덤 문자열 생성")
  void privateECKey() throws JOSEException {
    OctetKeyPair privateKey = new OctetKeyPairGenerator(Curve.Ed25519)
        .keyUse(KeyUse.ENCRYPTION)
        .keyID(UUID.randomUUID().toString())
        .generate();

    Base64URL privateKeyX = privateKey.getX();

    System.out.println("privateKeyX = " + privateKeyX);
    System.out.println("privateKeyX.toString() = " + privateKeyX.toString());
  }

  @Test
  @DisplayName("")
  void ve() {
    String refreshToken = "eyJhbGciOiJFZERTQSJ9.eyJzdWIiOiJwYW5kYTQ1M0BnbWFpbC5jb20iLCJleHAiOjE2ODE0OTAxMTMsImF1dGhvcml0aWVzIjoiTUFOQUdFUiJ9.jIbMvHqMROvNXA8Y5e1avjUQjTfkDCUQMDFxr_6LfbpjPsORw8M78RT5x3AwmeUbzeXoDF7N57qOJmd-6kgCBQ";
    boolean valid = ellipticCurveVerifier.isValid(refreshToken);
    System.out.println("valid = " + valid);
  }
}