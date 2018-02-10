package sp1d.luxnotifier.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sp1d.luxnotifier.LuxnotifierRuntimeException;
import sp1d.luxnotifier.parser.AvailableVisit;
import sp1d.luxnotifier.parser.AvailableVisitsParser;
import sp1d.luxnotifier.parser.SearchPageParser;
import sp1d.luxnotifier.parser.SimpleParser;
import sp1d.luxnotifier.request.LoginRequestSender;
import sp1d.luxnotifier.request.SearchPageRequestSender;
import sp1d.luxnotifier.request.SearchTimeslotRequestSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Action {

    @Autowired
    private LoginRequestSender login;
    @Autowired
    private SearchTimeslotRequestSender search;
    @Autowired
    private SearchPageRequestSender searchPage;


    public void act() {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("login", "buzzband@gmail.com");
        authParams.put("password", "HrenVam4745");
        String loginResponse = login.send(authParams);
        validateLoginResponse(loginResponse);

        String searchPageResponse = searchPage.send();
        String verificationTokenValue = SimpleParser.parseVerificationToken(searchPageResponse);

        String searchResponse = search.send(Collections.singletonMap("verificationToken", verificationTokenValue));
        validateSearchResponse(searchResponse);

        Map<String, String> services = new SearchPageParser(searchResponse).parseServices();

        AvailableVisitsParser availableVisitsParser = AvailableVisitsParser.anAvailableVisitsParser(searchResponse);
        List<AvailableVisit> availableVisits = availableVisitsParser.parse();

        for (AvailableVisit availableVisit : availableVisits) {
            System.out.println(availableVisit);
        }
    }

    private void validateSearchResponse(String searchResponse) {
        if (searchResponse.equalsIgnoreCase("invalid response :(")) {
            throw new LuxnotifierRuntimeException("LoginAction response didn't pass validation. User may not be logged in.");
        }
    }

    private void validateLoginResponse(String loginResponse) {
        if (loginResponse.contains("currentUser dropdown") && loginResponse.contains("/PatientPortal/Reservations/Visits")) {
            return;
        }
        throw new LuxnotifierRuntimeException("LoginAction response didn't pass validation. User may not be logged in.");
    }
}
