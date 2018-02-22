package sp1d.luxnotifier.request;

import okhttp3.RequestBody;

import java.util.Map;

public class RequestParameters {
    private String url;
    private RequestBody requestBody;
    private Map<String, String> customParameters;
    private String method;

    public static Builder aRequestParameters() {
        return new Builder();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(Map<String, String> customParameters) {
        this.customParameters = customParameters;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public static final class Builder {
        private String url;
        private RequestBody requestBody;
        private Map<String, String> customParameters;

        private String method;

        private Builder() {
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withRequestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder withCustomParameters(Map<String, String> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public Builder withMethod(String method) {
            this.method = method;
            return this;
        }

        public RequestParameters build() {
            RequestParameters requestParameters = new RequestParameters();
            requestParameters.setUrl(url);
            requestParameters.setRequestBody(requestBody);
            requestParameters.setCustomParameters(customParameters);
            requestParameters.setMethod(method);
            return requestParameters;
        }
    }
}
