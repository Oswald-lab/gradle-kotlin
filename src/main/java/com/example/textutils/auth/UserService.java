package com.example.textutils.auth;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class UserService {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9._-]{3,32}");
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final Map<String, StoredUser> users = new ConcurrentHashMap<>();
    private final PasswordHasher passwordHasher;

    public UserService() {
        this(new PasswordHasher());
    }

    public UserService(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public User register(String username, String password) {
        return register(username, password, Role.USER);
    }

    public User register(String username, String password, Role role) {
        String displayName = validateUsername(username);
        validatePassword(password);
        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }

        String key = normalize(displayName);
        String salt = passwordHasher.newSalt();
        StoredUser stored = new StoredUser(
                new User(displayName, role, Instant.now()),
                salt,
                passwordHasher.hash(password, salt));

        StoredUser previous = users.putIfAbsent(key, stored);
        if (previous != null) {
            throw new AuthException("Username already exists: " + displayName);
        }
        return stored.user;
    }

    public User authenticate(String username, String password) {
        String key = normalize(validateUsername(username));
        if (password == null) {
            throw new AuthException("Invalid username or password");
        }
        StoredUser stored = users.get(key);
        if (stored == null || !passwordHasher.verify(password, stored.salt, stored.hash)) {
            throw new AuthException("Invalid username or password");
        }
        return stored.user;
    }

    public Optional<User> find(String username) {
        if (username == null) {
            return Optional.empty();
        }
        StoredUser stored = users.get(normalize(username));
        return stored == null ? Optional.empty() : Optional.of(stored.user);
    }

    public List<User> listUsers() {
        List<User> result = new ArrayList<>();
        for (StoredUser stored : users.values()) {
            result.add(stored.user);
        }
        result.sort((a, b) -> a.getUsername().compareToIgnoreCase(b.getUsername()));
        return result;
    }

    public boolean delete(String username) {
        if (username == null) {
            return false;
        }
        return users.remove(normalize(username)) != null;
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        String key = normalize(validateUsername(username));
        StoredUser stored = users.get(key);
        if (stored == null || !passwordHasher.verify(currentPassword, stored.salt, stored.hash)) {
            throw new AuthException("Invalid username or password");
        }
        validatePassword(newPassword);
        String salt = passwordHasher.newSalt();
        String hash = passwordHasher.hash(newPassword, salt);
        users.put(key, new StoredUser(stored.user, salt, hash));
    }

    private static String validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username must not be null");
        }
        String trimmed = username.trim();
        if (!USERNAME_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(
                    "username must be 3-32 characters and contain only letters, digits, '.', '_' or '-'");
        }
        return trimmed;
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
    }

    private static String normalize(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }

    private static final class StoredUser {
        private final User user;
        private final String salt;
        private final String hash;

        private StoredUser(User user, String salt, String hash) {
            this.user = user;
            this.salt = salt;
            this.hash = hash;
        }
    }
}
