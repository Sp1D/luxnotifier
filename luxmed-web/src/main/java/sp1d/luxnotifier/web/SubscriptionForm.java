package sp1d.luxnotifier.web;

public class SubscriptionForm {
    private String selectedServiceId;
    private String selectedLanguageId;

    public SubscriptionForm(String selectedServiceId, String selectedLanguageId) {
        this.selectedServiceId = selectedServiceId;
        this.selectedLanguageId = selectedLanguageId;
    }

    public String getSelectedServiceId() {
        return selectedServiceId;
    }

    public void setSelectedServiceId(String selectedServiceId) {
        this.selectedServiceId = selectedServiceId;
    }

    public String getSelectedLanguageId() {
        return selectedLanguageId;
    }

    public void setSelectedLanguageId(String selectedLanguageId) {
        this.selectedLanguageId = selectedLanguageId;
    }
}
