package com.example.textutils.auth;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class User {

    private final String id;
    private final String username;
    private final Role role;
    private final Instant createdAt;

    User(String username, Role role, Instant createdAt) {
        this.id = UUID.randomUUID().toString();
        this.username = Objects.requireNonNull(username, "username");
        this.role = Objects.requireNonNull(role, "role");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', username='" + username + "', role=" + role + "}";
    }
}
