package sp1d.luxnotifier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.rcp.RemoteAuthenticationException;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import sp1d.luxnotifier.request.LoginRequestSender;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class LuxmedAuthenticationManager implements RemoteAuthenticationManager {
    @Autowired
    private LoginRequestSender loginRequestSender;

    @Override
    public Collection<? extends GrantedAuthority> attemptAuthentication(String username, String password) throws RemoteAuthenticationException {
//        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
        Map<String, String> authMap = new HashMap<>();
        authMap.put("login", username);
        authMap.put("password", password);
        String loginResponse = loginRequestSender.send(authMap);
        if (!isValidLoginResponse(loginResponse)) {
            throw new RemoteAuthenticationException("User is not recognized by Luxmed system");
        }
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    private boolean isValidLoginResponse(String loginResponse) {
        return loginResponse.contains("currentUser dropdown") && loginResponse.contains("/PatientPortal/Reservations/Visits");
    }
}
