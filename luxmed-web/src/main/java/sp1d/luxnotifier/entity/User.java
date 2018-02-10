package sp1d.luxnotifier.entity;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class User {
    private String email;

    public Map<String, String> asMap() {
        return Collections.singletonMap("login", email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public static User.Builder anUser() {
        return new Builder();
    }

    public static class Builder {
        private String email;

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            User user = new User();
            user.setEmail(email);
            return user;
        }
    }
}
