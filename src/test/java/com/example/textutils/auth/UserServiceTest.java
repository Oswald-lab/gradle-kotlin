package com.example.textutils.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private final UserService users = new UserService();

    @Test
    void registersAndFindsUser() {
        User user = users.register("alice", "password123");

        assertEquals("alice", user.getUsername());
        assertEquals(Role.USER, user.getRole());
        assertTrue(users.find("alice").isPresent());
    }

    @Test
    void usernamesAreCaseInsensitive() {
        users.register("Alice", "password123");

        assertTrue(users.find("alice").isPresent());
        assertEquals("Alice", users.authenticate("ALICE", "password123").getUsername());
    }

    @Test
    void rejectsDuplicateUsernames() {
        users.register("alice", "password123");

        assertThrows(AuthException.class, () -> users.register("ALICE", "anotherpass"));
    }

    @Test
    void rejectsInvalidUsername() {
        assertThrows(IllegalArgumentException.class, () -> users.register("ab", "password123"));
        assertThrows(IllegalArgumentException.class, () -> users.register("bad name", "password123"));
    }

    @Test
    void rejectsShortPassword() {
        assertThrows(IllegalArgumentException.class, () -> users.register("alice", "short"));
    }

    @Test
    void authenticatesCorrectCredentials() {
        User registered = users.register("alice", "password123");

        assertEquals(registered, users.authenticate("alice", "password123"));
    }

    @Test
    void rejectsWrongPassword() {
        users.register("alice", "password123");

        assertThrows(AuthException.class, () -> users.authenticate("alice", "wrongpassword"));
        assertThrows(AuthException.class, () -> users.authenticate("nobody", "password123"));
    }

    @Test
    void doesNotStorePlainTextPassword() {
        UserService service = new UserService();
        service.register("alice", "password123");

        assertThrows(AuthException.class, () -> service.authenticate("alice", "password12"));
        assertDoesNotThrow(() -> service.authenticate("alice", "password123"));
    }

    @Test
    void changesPassword() {
        users.register("alice", "password123");

        users.changePassword("alice", "password123", "newpassword");

        assertThrows(AuthException.class, () -> users.authenticate("alice", "password123"));
        assertDoesNotThrow(() -> users.authenticate("alice", "newpassword"));
    }

    @Test
    void listsAndDeletesUsers() {
        users.register("carol", "password123");
        users.register("alice", "password123");

        assertEquals("alice", users.listUsers().get(0).getUsername());
        assertTrue(users.delete("carol"));
        assertFalse(users.find("carol").isPresent());
        assertFalse(users.delete("carol"));
    }

    @Test
    void registersAdmin() {
        User admin = users.register("root", "password123", Role.ADMIN);

        assertTrue(admin.isAdmin());
        assertEquals(Role.ADMIN, admin.getRole());
    }

    @Test
    void findReturnsEmptyForUnknownUser() {
        Optional<User> result = users.find("ghost");

        assertFalse(result.isPresent());
        assertFalse(users.find(null).isPresent());
    }
}
