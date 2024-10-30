package com.example.workoutsample;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "userpass1";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Encoded password: " + encodedPassword);

        String rawPassword2 = "adminpass1";
        String encodedPassword2 = encoder.encode(rawPassword2);

        System.out.println("Encoded password: " + encodedPassword2);
    }
}
