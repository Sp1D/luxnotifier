package sp1d.luxnotifier.request;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sp1d.luxnotifier.Luxnotifier;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Luxnotifier.class)
public class LoginRequestFunctionalTest {
    @Autowired
    private LoginRequestSender loginHttpRequest;
    @Autowired
    private Environment env;

    @Test
    public void loginsSuccessfully() throws Exception {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("login", env.getProperty("login"));
        userMap.put("password", env.getProperty("password"));
        String responseBody = loginHttpRequest.send(userMap);
        assertThat(responseBody).contains("currentUser dropdown").contains("/PatientPortal/Reservations/Visits");
    }
}