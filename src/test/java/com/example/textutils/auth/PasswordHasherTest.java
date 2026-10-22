package com.example.textutils.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    private final PasswordHasher hasher = new PasswordHasher();

    @Test
    void hashesAndVerifiesPassword() {
        String salt = hasher.newSalt();
        String hash = hasher.hash("password123", salt);

        assertNotEquals("password123", hash);
        assertTrue(hasher.verify("password123", salt, hash));
        assertFalse(hasher.verify("wrongpassword", salt, hash));
    }

    @Test
    void samePasswordWithDifferentSaltsProducesDifferentHashes() {
        String firstSalt = hasher.newSalt();
        String secondSalt = hasher.newSalt();

        assertNotEquals(firstSalt, secondSalt);
        assertNotEquals(hasher.hash("password123", firstSalt), hasher.hash("password123", secondSalt));
    }
}
