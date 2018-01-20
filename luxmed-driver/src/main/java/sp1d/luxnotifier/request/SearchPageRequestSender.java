package sp1d.luxnotifier.request;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SearchPageRequestSender extends RequestSender {
    public SearchPageRequestSender(OkHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    protected RequestParameters buildRequestParameters(Map<String, String> customParameters) {
        return RequestParameters.builder()
                .method("GET")
                .url("https://portalpacjenta.luxmed.pl/PatientPortal/Reservations/Coordination/Activity?actionId=90")
                .build();
    }
}
