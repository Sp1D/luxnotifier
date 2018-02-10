package sp1d.luxnotifier.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.authentication.rcp.RemoteAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private RemoteAuthenticationManager remAuthManager;

    public SecurityConfiguration(RemoteAuthenticationManager remAuthManager) {
        this.remAuthManager = remAuthManager;
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/**")
//                .hasRole("USER")А
//                .and()
//                .formLogin();
//    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        RemoteAuthenticationProvider remAuthProvider = new RemoteAuthenticationProvider();
        remAuthProvider.setRemoteAuthenticationManager(remAuthManager);
        return remAuthProvider;
    }
}
