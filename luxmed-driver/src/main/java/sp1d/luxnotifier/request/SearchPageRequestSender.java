package sp1d.luxnotifier.request;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.util.Map;

import static sp1d.luxnotifier.request.RequestParameters.aRequestParameters;

@Component
public class SearchPageRequestSender extends RequestSender {
    public SearchPageRequestSender(OkHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    protected RequestParameters buildRequestParameters(Map<String, String> customParameters) {
        return aRequestParameters()
                .withMethod("GET")
                .withUrl("https://portalpacjenta.luxmed.pl/PatientPortal/Reservations/Coordination/Activity?actionId=90")
                .build();
    }
}
