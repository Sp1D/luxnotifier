package sp1d.luxnotifier.request;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Map;

import static sp1d.luxnotifier.request.RequestParameters.aRequestParameters;

@Component
public class LoginRequestSender extends RequestSender {

    public LoginRequestSender(OkHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    protected RequestParameters buildRequestParameters(Map<String, String> customParameters) {
        FormBody urlEncodedForm = new FormBody.Builder(Charset.forName("UTF-8"))
                .add("login", getParameter(customParameters, "login"))
                .add("password", getParameter(customParameters, "password"))
                .build();
        return aRequestParameters()
                .withRequestBody(urlEncodedForm)
                .withUrl("https://portalpacjenta.luxmed.pl/PatientPortal/Account/LogIn")
                .withMethod("POST")
                .build();
    }
}
