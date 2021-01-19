package io.example.authorization.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UtilTest {
    @Test
    @DisplayName("ClientSecret 발급 Test")
    public void generateClientSecret(){
        String generateClientKey = "1:naver:"+ UUID.randomUUID().toString();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String clientSecret = passwordEncoder.encode(generateClientKey);
        System.out.println("[#1] result : " + clientSecret);

        String generateClientKey2 = "1:naver:"+ UUID.randomUUID().toString();
        String clientSecret2 = passwordEncoder.encode(generateClientKey2);
        System.out.println("[#2] result : " + clientSecret2);

    }
}