package com.clinicapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        String password = "password";
        String hash = new BCryptPasswordEncoder().encode(password);
        System.out.println("Hash gerado: " + hash);
    }
}