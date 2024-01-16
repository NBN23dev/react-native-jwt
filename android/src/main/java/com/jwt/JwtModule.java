package com.jwt;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;

@ReactModule(name = JwtModule.NAME)
public class JwtModule extends ReactContextBaseJavaModule {
    public JwtModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    private static RSAKey getPrivateKeyFromPEM(String key) throws GeneralSecurityException {
        String privateKeyPEM = key;

        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.decode(privateKeyPEM, 0);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

        return (RSAKey) keyFactory.generatePrivate(keySpec);
    }

    @ReactMethod
    public void sign(ReadableMap header, ReadableMap payload, String privateKey, Promise promise) {
        try {
            RSAKey key = getPrivateKeyFromPEM(privateKey);

            Algorithm algorithm = Algorithm.RSA256(key);

            String token = JWT.create()
                    .withHeader(header.toHashMap())
                    .withIssuer(payload.getString("iss"))
                    .withSubject(payload.getString("sub"))
                    .withAudience(payload.getString("aud"))
                    .withClaim("iat", payload.getDouble("iat"))
                    .withClaim("exp", payload.getDouble("exp"))
                    .sign(algorithm);

            promise.resolve(token);
        } catch (Exception exception){
            promise.reject(exception);
        }
    }
}
