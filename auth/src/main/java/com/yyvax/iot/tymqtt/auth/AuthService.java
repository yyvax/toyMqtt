package com.yyvax.iot.tymqtt.auth;

import org.springframework.stereotype.Service;

import java.security.KeyPairGenerator;

@Service
public class AuthService implements IAuthService {
    @Override
    public boolean check(String username, String password) {
        return true;
    }

    public String generateKey(String username) throws Exception {
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(512);
        byte[] publicKey = keyPairGenerator.genKeyPair().getPrivate().getEncoded();
        StringBuilder sb = new StringBuilder();
        for (byte b : publicKey) {
            sb.append(Integer.toHexString(0x0100 + (b & 0x00FF)).substring(1));
        }
        return sb.toString().substring(50, 60);
    }

}
