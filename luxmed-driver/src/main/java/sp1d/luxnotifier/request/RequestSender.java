package sp1d.luxnotifier.request;


import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import sp1d.luxnotifier.LuxnotifierRuntimeException;

import java.util.*;

public abstract class RequestSender {
    private static final Set<String> ALLOWED_HTTP_METHOD = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("POST", "GET"))
    );

    private OkHttpClient httpClient;

    @Autowired
    public RequestSender(OkHttpClient httpClient) {
        if (httpClient == null) {
            throw new IllegalArgumentException("OkHttpClient mustn't be null");
        }
        this.httpClient = httpClient;
    }

    public String send() {
        return send(null);
    }

    public String send(Map<String, String> customParameters) {
        Call call = httpClient.newCall(buildRequest(customParameters));
        try (Response response = call.execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                return "";
            }
        } catch (Exception ex) {
            throw new LuxnotifierRuntimeException(ex);
        }
    }

    private Request buildRequest(Map<String, String> customParameters) {
        RequestParameters parameters = buildRequestParameters(customParameters);
        if (!ALLOWED_HTTP_METHOD.contains(parameters.getMethod())) {
            throw new LuxnotifierRuntimeException(
                    new IllegalArgumentException(
                            String.format("HTTP method %s is invalid", parameters.getMethod())
                    )
            );
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(parameters.getUrl())
                .addHeader("Accept", "*/*")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36" +
                        " (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        if (parameters.getMethod().equalsIgnoreCase("POST")) {
            requestBuilder.post(parameters.getRequestBody());
        } else if (parameters.getMethod().equalsIgnoreCase("GET")) {
            requestBuilder.get();
        }
        return requestBuilder.build();
    }

    protected abstract @NonNull
    RequestParameters buildRequestParameters(Map<String, String> customParameters);
}
