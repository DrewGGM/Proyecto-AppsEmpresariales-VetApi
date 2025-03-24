package com.vetapi.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


// Service for hashing and verifying passwords using BCrypt algorithm
@Service
public class BcryptHashService {

    private final BCryptPasswordEncoder passwordEncoder;

    public BcryptHashService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Encrypts a plain text password using BCrypt
     *
     * @param plainPassword the password to encrypt
     * @return the encrypted password
     */
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * Verifies if a plain text password matches a hashed password
     *
     * @param plainPassword  the plain text password to check
     * @param hashedPassword the hashed password to compare against
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}