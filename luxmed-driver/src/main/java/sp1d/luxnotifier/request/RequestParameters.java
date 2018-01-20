package sp1d.luxnotifier.request;

import lombok.Builder;
import lombok.Data;
import okhttp3.RequestBody;

import java.util.Map;

@Data
@Builder
public class RequestParameters {
    private String url;
    private RequestBody requestBody;
    private Map<String, String> customParameters;
    private String method;
}
