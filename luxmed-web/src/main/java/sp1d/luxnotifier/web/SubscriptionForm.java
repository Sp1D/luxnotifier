package sp1d.luxnotifier.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionForm {
    private String selectedServiceId;
    private String selectedLanguageId;
    private boolean bookingEnabled;
}
