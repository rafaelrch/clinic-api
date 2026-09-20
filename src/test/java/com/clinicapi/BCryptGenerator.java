package com.clinicapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        String senha = "password";
        String hash = new BCryptPasswordEncoder().encode(senha);
        System.out.println("Hash gerado: " + hash);
    }
}