package com.hn.api.diary.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class PasswordEncoder {

    private static final SCryptPasswordEncoder ENCODER = new SCryptPasswordEncoder(
            16, 8, 1, 32, 64
    );

    public static String encrypt(String rawPassword){
        return ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encryptedPassword){
        return ENCODER.matches(rawPassword, encryptedPassword);
    }
}
