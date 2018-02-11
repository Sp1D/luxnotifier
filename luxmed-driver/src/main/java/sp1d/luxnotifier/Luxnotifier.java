package sp1d.luxnotifier;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import sp1d.luxnotifier.action.Action;

@Configuration
@ComponentScan
@PropertySource("classpath:luxmed-driver.properties")
public class Luxnotifier {
    private final static Logger LOG = LoggerFactory.getLogger(Luxnotifier.class);
    private final static Logger LOG_HTTP_CLIENT = LoggerFactory.getLogger(OkHttpClient.class);
    private Environment env;

    public Luxnotifier(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Luxnotifier.class);
        Action action = ctx.getBean(Action.class);
        action.act();
    }

    @Bean
    @Scope("session")
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

}
