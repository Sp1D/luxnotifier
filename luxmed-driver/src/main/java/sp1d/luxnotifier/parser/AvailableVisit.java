package sp1d.luxnotifier.parser;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

public class AvailableVisit {
    private String doctor;
    private LocalDateTime dateTime;
    private String clinic;
    private String service;

    public AvailableVisit() {
    }

    public AvailableVisit(String doctor, LocalDateTime dateTime, String clinic, String service) {
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.clinic = clinic;
        this.service = service;
    }

    public static Builder anAvailableVisit() {
        return new Builder();
    }

    public String getDoctor() {
        return doctor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getClinic() {
        return clinic;
    }

    public String getService() {
        return service;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableVisit that = (AvailableVisit) o;
        return Objects.equals(doctor, that.doctor) &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(clinic, that.clinic) &&
                Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctor, dateTime, clinic, service);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("doctor", doctor)
                .append("dateTime", dateTime)
                .append("clinic", clinic)
                .append("service", service)
                .toString();
    }

    public static final class Builder {
        private String doctor;
        private LocalDateTime dateTime;
        private String clinic;
        private String service;

        private Builder() {
        }

        public Builder withDoctor(String doctor) {
            this.doctor = doctor;
            return this;
        }

        public Builder withDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Builder withClinic(String clinic) {
            this.clinic = clinic;
            return this;
        }

        public Builder withService(String service) {
            this.service = service;
            return this;
        }

        public AvailableVisit build() {
            return new AvailableVisit(
                    doctor,
                    dateTime,
                    clinic,
                    service
            );
        }
    }
}
