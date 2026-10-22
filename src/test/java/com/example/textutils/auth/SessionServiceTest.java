package com.example.textutils.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class SessionServiceTest {

    private final UserService users = new UserService();
    private final SessionService sessions = new SessionService(users);

    @Test
    void loginReturnsValidToken() {
        User user = users.register("alice", "password123");

        String token = sessions.login("alice", "password123");

        assertNotNull(token);
        assertEquals(user, sessions.validate(token).orElseThrow());
    }

    @Test
    void loginRejectsBadCredentials() {
        users.register("alice", "password123");

        assertThrows(AuthException.class, () -> sessions.login("alice", "wrongpassword"));
    }

    @Test
    void logoutInvalidatesToken() {
        users.register("alice", "password123");
        String token = sessions.login("alice", "password123");

        assertTrue(sessions.logout(token));
        assertFalse(sessions.validate(token).isPresent());
        assertFalse(sessions.logout(token));
    }

    @Test
    void unknownTokenIsInvalid() {
        assertFalse(sessions.validate("missing").isPresent());
        assertFalse(sessions.validate(null).isPresent());
    }

    @Test
    void expiredTokenIsInvalid() {
        users.register("alice", "password123");
        SessionService shortLived = new SessionService(users, Duration.ZERO);
        String token = shortLived.login("alice", "password123");

        assertFalse(shortLived.validate(token).isPresent());
    }
}
