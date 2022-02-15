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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

class EllipticCurveKeyTest {

    @Test
    @DisplayName("ECC private / public key 테스트")
    void jwtTest() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, JOSEException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"));

        final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        System.out.println("ecKey.publicKey: " + Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
        System.out.println("ecKey.privateKey: " + Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
    }

    @Test
    @DisplayName("ED25519 private / public key 테스트")
    void ed25519Test() throws JOSEException, ParseException {
        OctetKeyPair jwk = new OctetKeyPairGenerator(Curve.Ed25519)
                .keyUse(KeyUse.SIGNATURE)
                .keyID("panda")
                .generate();
        OctetKeyPair publicJWK = jwk.toPublicJWK();

        Ed25519Signer signer = new Ed25519Signer(jwk);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("pandabear")
                .issuer("https://panda.com")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .build();

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.EdDSA)
                .keyID(jwk.getKeyID())
                .build();

        SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);

        signedJWT.sign(signer);

        String serializedToken = signedJWT.serialize();
        System.out.println("serializedToken = " + serializedToken);

        signedJWT = SignedJWT.parse(serializedToken);
        System.out.println("signedJWT = " + signedJWT.getJWTClaimsSet());

        Ed25519Verifier verifier = new Ed25519Verifier(publicJWK);
        System.out.println("isVerified = " + signedJWT.verify(verifier));
    }

    @Test
    @DisplayName("token-secret 생성")
    void createTokenSecret() {
        UUID tokenSecret = UUID.randomUUID();
        System.out.println("tokenSecret = " + tokenSecret);
    }
}
