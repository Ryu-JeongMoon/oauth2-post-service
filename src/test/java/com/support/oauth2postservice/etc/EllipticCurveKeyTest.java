package com.support.oauth2postservice.etc;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.codec.binary.Base64;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@DisplayName("타원 곡선 알고리즘 테스트")
class EllipticCurveKeyTest {

  private OctetKeyPair publicJWK;
  private OctetKeyPair privateJWK;
  private SignedJWT signedJWT;
  private Ed25519Verifier verifier;
  private String serializedToken;

  @BeforeEach
  @DisplayName("private & public key 생성")
  void setUp() throws JOSEException {
    privateJWK = new OctetKeyPairGenerator(Curve.Ed25519)
        .keyUse(KeyUse.SIGNATURE)
        .keyID(UUID.randomUUID().toString())
        .generate();

    Ed25519Signer signer = new Ed25519Signer(privateJWK);
    publicJWK = privateJWK.toPublicJWK();

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject("pandabear")
        .issuer("https://panda.com")
        .expirationTime(new Date(new Date().getTime() + 60 * 1000))
        .build();

    JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.EdDSA)
        .keyID(privateJWK.getKeyID())
        .build();

    signedJWT = new SignedJWT(jwsHeader, claimsSet);
    signedJWT.sign(signer);
    verifier = new Ed25519Verifier(publicJWK);
  }

  @Test
  @DisplayName("비밀키에 'd' parameter (token-secret) 존재해야 한다")
  void privateJWK() {
    Assertions.assertThat(privateJWK.getD()).isNotNull();
  }

  @Test
  @DisplayName("공개키에 'd' parameter 존재하지 않아야 한다")
  void publicJWK() {
    Assertions.assertThat(publicJWK.getD()).isNull();
  }

  @Test
  @DisplayName("서명된 토큰 직렬화")
  void tokenSerialization() {
    serializedToken = signedJWT.serialize();
    Assertions.assertThat(serializedToken).isNotBlank();
  }

  @Test
  @DisplayName("서명된 토큰 역직렬화 후 검증")
  void ed25519Test() throws ParseException, JOSEException {
    serializedToken = signedJWT.serialize();
    signedJWT = SignedJWT.parse(serializedToken);

    boolean isVerified = signedJWT.verify(verifier);
    Assertions.assertThat(isVerified).isTrue();
  }

  @Nested
  @DisplayName("ECC secret & key 생성")
  class OtherWayTest {

    @Test
    @DisplayName("token-secret 36 bytes 랜덤 생성")
    void createTokenSecret() {
      UUID tokenSecret = UUID.randomUUID();
      Assertions.assertThat(tokenSecret.toString().length()).isEqualTo(36);
    }

    @Test
    @DisplayName("KeyPairGenerator.getInstance() 생성 테스트")
    void usingKeyPairGeneratorGetInstance() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
      keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"));
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      System.out.println("ecKey.publicKey     => " + Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
      System.out.println("ecKey.privateKey    => " + Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
    }
  }
}

/*
kty => key type
d   => ECC private key
crv => curve parameter
kid => key id
x/y => point (x, y)

jwk = {
  "kty":"OKP",
  "d":"3shGAwPDVpz31nz_tC6IypS8Q3-AaAoeKp3o0_ziAA8",
  "use":"sig",
  "crv":"Ed25519",
  "kid":"panda",
  "x":"Gai7nhnN6ZyRpBRQRZp0nekfM70QiaG6-jfuZSkaSrw"
}
 */