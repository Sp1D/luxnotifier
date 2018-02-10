package sp1d.luxnotifier.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

public class Subscription {
    private String userEmail;
    private String serviceId;
    private String serviceName;
    private String languageId;
    private String languageName;
    private boolean bookingEnabled;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchUntilDate;

    public String getUserEmail() {
        return userEmail;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getLanguageId() {
        return languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public boolean isBookingEnabled() {
        return bookingEnabled;
    }

    public LocalDate getSearchUntilDate() {
        return searchUntilDate;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void setBookingEnabled(boolean bookingEnabled) {
        this.bookingEnabled = bookingEnabled;
    }

    public void setSearchUntilDate(LocalDate searchUntilDate) {
        this.searchUntilDate = searchUntilDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(userEmail, that.userEmail) &&
                Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, serviceId);
    }

    public static Builder aSubscription() {
        return new Builder();
    }

    public static final class Builder {
        private String userEmail;
        private String serviceId;
        private String serviceName;
        private String languageId;
        private String languageName;
        private boolean bookingEnabled;
        private LocalDate searchUntilDate;

        private Builder() {
        }

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder withServiceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder withLanguageId(String languageId) {
            this.languageId = languageId;
            return this;
        }

        public Builder withLanguageName(String languageName) {
            this.languageName = languageName;
            return this;
        }

        public Builder withBookingEnabled(boolean bookingEnabled) {
            this.bookingEnabled = bookingEnabled;
            return this;
        }

        public Builder withSearchUntilDate(LocalDate searchUntilDate) {
            this.searchUntilDate = searchUntilDate;
            return this;
        }

        public Subscription build() {
            Subscription subscription = new Subscription();
            subscription.userEmail = this.userEmail;
            subscription.serviceName = this.serviceName;
            subscription.serviceId = this.serviceId;
            subscription.languageName = this.languageName;
            subscription.languageId = this.languageId;
            subscription.searchUntilDate = this.searchUntilDate;
            subscription.bookingEnabled = this.bookingEnabled;
            return subscription;
        }
    }
}
