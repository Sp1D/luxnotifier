package sp1d.luxnotifier.parser;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AvailableVisit {
    private String doctor;
    private LocalDateTime dateTime;
    private String clinic;
    private String service;
}
