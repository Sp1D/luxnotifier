package sp1d.luxnotifier.request;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import sp1d.luxnotifier.LuxnotifierRuntimeException;

import java.nio.charset.Charset;
import java.util.Map;

@Component
public class LoginRequestSender extends RequestSender {

    public LoginRequestSender(OkHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    protected RequestParameters buildRequestParameters(Map<String, String> customParameters) {
        if (customParameters == null) {
            throw new LuxnotifierRuntimeException("Parameters with Login and Password must be specified");
        }
        FormBody requestBody = new FormBody.Builder(Charset.forName("UTF-8"))
                .add("login", customParameters.get("login"))
                .add("password", customParameters.get("password"))
                .build();
        return RequestParameters.builder()
                .requestBody(requestBody)
                .url("https://portalpacjenta.luxmed.pl/PatientPortal/Account/LogIn")
                .method("POST")
                .build();
    }
}
