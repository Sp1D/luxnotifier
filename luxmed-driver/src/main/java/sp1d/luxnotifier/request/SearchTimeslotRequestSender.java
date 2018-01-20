package sp1d.luxnotifier.request;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sp1d.luxnotifier.LuxnotifierRuntimeException;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.time.LocalDate.now;

@Component
public class SearchTimeslotRequestSender extends RequestSender {
    private final static DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    private Environment env;

    public SearchTimeslotRequestSender(OkHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    protected RequestParameters buildRequestParameters(Map<String, String> customParameters) {
        String verificationToken = getVerificationToken(customParameters);
        FormBody requestBody = new FormBody.Builder(Charset.forName("UTF-8"))
                .add("ReferralId", "")
                .add("DateOption", "SelectedDate")
                .add("PayersCount", "1")
                .add("CityId", "3")
                .add("ServiceId", "4549")
                .add("LanguageId", "11")
                .add("SearchFirstFree", "false")
                .add("FromDate", DD_MM_YYYY.format(now()))
                .add("ToDate", DD_MM_YYYY.format(now().plusMonths(1).minusDays(1)))
                .add("TimeOption", "Any")
                .add("PayerId", "45185")
                .add("__RequestVerificationToken", verificationToken)
                .add("IsDisabled", "false")
                .build();
        return RequestParameters.builder()
                .url("https://portalpacjenta.luxmed.pl/PatientPortal/Reservations/Reservation/Find")
                .requestBody(requestBody)
                .method("POST")
                .build();
    }

    private String getVerificationToken(Map<String, String> customParameters) {
        if (customParameters != null && !StringUtils.isEmpty(customParameters.get("verificationToken"))) {
            return customParameters.get("verificationToken");
        } else {
            throw new LuxnotifierRuntimeException(new IllegalArgumentException("Verification token is null or empty!"));
        }
    }
}
