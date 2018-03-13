package sp1d.luxnotifier.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class User {
    private String email;
    private String password;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("email", email)
                .toString();
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
