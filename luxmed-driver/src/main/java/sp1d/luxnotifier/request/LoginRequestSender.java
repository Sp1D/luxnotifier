package sp1d.luxnotifier.request;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Map;

@Component
public class LoginRequestSender extends RequestSender {
    @Autowired
    private Environment env;

    public LoginRequestSender(OkHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    protected RequestParameters buildRequestParameters(Map<String, String> customParameters) {
        FormBody requestBody = new FormBody.Builder(Charset.forName("UTF-8"))
                .add("login", env.getProperty("login"))
                .add("password", env.getProperty("password"))
                .build();
        return RequestParameters.builder()
                .requestBody(requestBody)
                .url("https://portalpacjenta.luxmed.pl/PatientPortal/Account/LogIn")
                .method("POST")
                .build();
    }
}
