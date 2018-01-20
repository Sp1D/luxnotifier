package sp1d.luxnotifier;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import sp1d.luxnotifier.action.Action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan
@PropertySource("classpath:luxnotifier.properties")
public class Luxnotifier {
    private final static Logger LOG = LoggerFactory.getLogger(Luxnotifier.class);
    private final static Logger LOG_HTTP_CLIENT = LoggerFactory.getLogger(OkHttpClient.class);
    @Autowired
    private Environment env;


    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Luxnotifier.class);
        Action action = ctx.getBean(Action.class);
        action.act();
    }

    @Bean
    public OkHttpClient httpClient() {
        LOG.debug("creating OkHttpClient instance");
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .followRedirects(true)
                .followSslRedirects(true)
                .cookieJar(new LuxCookieJar())

                .build();
    }

    @Bean
    public Interceptor loggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.valueOf(env.getProperty("httpclient.log.level"));
        return new HttpLoggingInterceptor(LOG_HTTP_CLIENT::debug).setLevel(loggingLevel);
    }

    @Bean
    public CookieJar cookieJar() {
        return new CookieJar() {
            private final Logger LOG = LoggerFactory.getLogger(this.getClass());
            private Map<String, List<Cookie>> cookieStorage = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                LOG.debug("Saving cookies {}", cookies);
                cookieStorage.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStorage.get(url.host());
                if (cookies != null) {
                    return cookies;
                }
                return Collections.emptyList();
            }
        };
    }
}
