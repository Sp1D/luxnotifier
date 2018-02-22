package sp1d.luxnotifier;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan
@PropertySource("classpath:luxmed-driver.properties")
public class LuxnotifierConfiguration {
    private final static Logger LOG = LoggerFactory.getLogger(LuxnotifierConfiguration.class);
    private final static Logger LOG_HTTP_CLIENT = LoggerFactory.getLogger(OkHttpClient.class);
    private Environment env;

    public LuxnotifierConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public OkHttpClient httpClient() {
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
}
