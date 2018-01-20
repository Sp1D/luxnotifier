package sp1d.luxnotifier.request;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sp1d.luxnotifier.Luxnotifier;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Luxnotifier.class)
public class LoginRequestFunctionalTest {
    @Autowired
    private LoginRequestSender loginHttpRequest;

    @Test
    public void loginsSuccessfully() throws Exception {
        String responseBody = loginHttpRequest.send();
        assertThat(responseBody).contains("currentUser dropdown").contains("/PatientPortal/Reservations/Visits");
    }
}