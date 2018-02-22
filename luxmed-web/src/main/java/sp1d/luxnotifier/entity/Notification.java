package sp1d.luxnotifier.entity;

import sp1d.luxnotifier.parser.AvailableVisit;

import java.util.Objects;

public class Notification {
    private String userEmail;
    private AvailableVisit availableVisit;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public AvailableVisit getAvailableVisit() {
        return availableVisit;
    }

    public void setAvailableVisit(AvailableVisit availableVisit) {
        this.availableVisit = availableVisit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(userEmail, that.userEmail) &&
                Objects.equals(availableVisit, that.availableVisit);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userEmail, availableVisit);
    }
}
