package sp1d.luxnotifier.request;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Map;

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
        return RequestParameters.builder()
                .requestBody(urlEncodedForm)
                .url("https://portalpacjenta.luxmed.pl/PatientPortal/Account/LogIn")
                .method("POST")
                .build();
    }
}
