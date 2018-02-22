package sp1d.luxnotifier.request;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.time.LocalDate.now;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static sp1d.luxnotifier.request.RequestParameters.aRequestParameters;

@Component
public class SearchTimeslotRequestSender extends RequestSender {
    public final static DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    private Environment env;

    public SearchTimeslotRequestSender(OkHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    protected RequestParameters buildRequestParameters(Map<String, String> customParameters) {
        String verificationToken = getParameter(customParameters, "verificationToken");
        String serviceId = getParameter(customParameters, "serviceId");
        String languageId = getParameter(customParameters, "languageId");
        String untilDate = firstNonNull(customParameters.get("searchUntilDate"), DD_MM_YYYY.format(now().plusMonths(1).minusDays(1)));

        FormBody requestBody = new FormBody.Builder(Charset.forName("UTF-8"))
                .add("ReferralId", "")
                .add("DateOption", "SelectedDate")
                .add("PayersCount", "1")
                .add("CityId", "3")
                .add("ServiceId", serviceId)
                .add("LanguageId", languageId)
                .add("SearchFirstFree", "false")
                .add("FromDate", DD_MM_YYYY.format(now()))
                .add("ToDate", untilDate)
                .add("TimeOption", "Any")
                .add("PayerId", "45185")
                .add("__RequestVerificationToken", verificationToken)
                .add("IsDisabled", "false")
                .build();
        return aRequestParameters()
                .withUrl("https://portalpacjenta.luxmed.pl/PatientPortal/Reservations/Reservation/Find")
                .withRequestBody(requestBody)
                .withMethod("POST")
                .build();
    }
}
