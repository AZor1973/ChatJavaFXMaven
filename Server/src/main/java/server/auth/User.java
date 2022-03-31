package server.auth;

import java.util.Objects;

public class User {

    private final String login;
    private final String password;
    private final String username;

    private User(Builder builder) {
        login = builder.login;
        password = builder.password;
        username = builder.username;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(User copy) {
        Builder builder = new Builder();
        builder.login = copy.getLogin();
        builder.password = copy.getPassword();
        builder.username = copy.getUsername();
        return builder;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    public static final class Builder {
        private String login;
        private String password;
        private String username;

        private Builder() {
        }

        public Builder withLogin(String val) {
            login = val;
            return this;
        }

        public Builder withPassword(String val) {
            password = val;
            return this;
        }

        public Builder withUsername(String val) {
            username = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
