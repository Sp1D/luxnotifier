package sp1d.luxnotifier.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    private String email;
    private String password;

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();
        map.put("login", email);
        map.put("password", password);
        return Collections.unmodifiableMap(map);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    public static User.Builder anUser() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String password;

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            return user;
        }
    }
}
