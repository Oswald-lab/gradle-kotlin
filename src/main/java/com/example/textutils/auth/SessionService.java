package com.example.textutils.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionService {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final UserService userService;
    private final Duration ttl;

    public SessionService(UserService userService) {
        this(userService, DEFAULT_TTL);
    }

    public SessionService(UserService userService, Duration ttl) {
        this.userService = Objects.requireNonNull(userService, "userService");
        this.ttl = Objects.requireNonNull(ttl, "ttl");
    }

    public String login(String username, String password) {
        User user = userService.authenticate(username, password);
        String token = UUID.randomUUID().toString();
        sessions.put(token, new Session(user, Instant.now().plus(ttl)));
        return token;
    }

    public Optional<User> validate(String token) {
        if (token == null) {
            return Optional.empty();
        }
        Session session = sessions.get(token);
        if (session == null) {
            return Optional.empty();
        }
        if (session.expiresAt.isBefore(Instant.now())) {
            sessions.remove(token);
            return Optional.empty();
        }
        return Optional.of(session.user);
    }

    public boolean logout(String token) {
        return token != null && sessions.remove(token) != null;
    }

    private static final class Session {
        private final User user;
        private final Instant expiresAt;

        private Session(User user, Instant expiresAt) {
            this.user = user;
            this.expiresAt = expiresAt;
        }
    }
}
