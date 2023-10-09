package ua.training.model.entity;

import ua.training.model.entity.enums.Role;

public class User {

    private long id;
    private String login;
    private String passwordHash;
    private Role role;
    private boolean isBlocked;

    public static class Builder {
        private long id;
        private String login;
        private String passwordHash;
        private Role role;
        private boolean isBlocked;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password_hash(String password_hash) {
            this.passwordHash = password_hash;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder isBlocked(boolean blocked) {
            isBlocked = blocked;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.login = builder.login;
        this.passwordHash = builder.passwordHash;
        this.role = builder.role;
        this.isBlocked = builder.isBlocked;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
