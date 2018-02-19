package sp1d.luxnotifier.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import sp1d.luxnotifier.dao.SubscriptionDao;
import sp1d.luxnotifier.dao.UserDao;
import sp1d.luxnotifier.entity.Subscription;
import sp1d.luxnotifier.entity.User;
import sp1d.luxnotifier.parser.AvailableVisit;
import sp1d.luxnotifier.parser.SimpleParser;
import sp1d.luxnotifier.request.LoginRequestSender;
import sp1d.luxnotifier.request.SearchPageRequestSender;
import sp1d.luxnotifier.request.SearchTimeslotRequestSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sp1d.luxnotifier.parser.AvailableVisitsParser.anAvailableVisitsParser;
import static sp1d.luxnotifier.request.SearchTimeslotRequestSender.DD_MM_YYYY;

@Component
public class ScheduledVisitFinder {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledVisitFinder.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private SubscriptionDao subscriptionDao;
    @Autowired
    private UserNotifier notifier;
    @Autowired
    private LoginRequestSender loginRequest;
    @Autowired
    private SearchPageRequestSender searchRequest;
    @Autowired
    private SearchTimeslotRequestSender timeslotRequest;

    @Scheduled(cron = "0 */2 5-23 * * *")
    public void find() {
        LOG.info("Scheduled visit search is started");
        for (User user : userDao.findAll()) {
            List<Subscription> subscriptions = subscriptionDao.findByUserEmail(user.getEmail());
            if (CollectionUtils.isEmpty(subscriptions)) {
                LOG.info("User {} has no subscriptions", user.getEmail());
                continue;
            }
            LOG.info("Searching for visits subscribed by {}", user.getEmail());
            loginUser(user);
            String verificationToken = parseVerificationToken();
            for (Subscription subscription : subscriptions) {
                LOG.info("Searching for {}", subscription.getServiceName());
                List<AvailableVisit> availableVisits = loadAndParseAvailableVisits(subscription, verificationToken);
                if (availableVisits.size() > 0) {
                    LOG.info("Visit of {} is possible", subscription.getServiceName());
                    notifier.notifyUser(subscription, availableVisits);
                }
            }
        }
        LOG.info("Scheduled visit search is stopped");
    }

    private void loginUser(User user) {
        loginRequest.send(userToMap(user));
    }

    private Map<String, String> userToMap(User user) {
        Map<String, String> map = new HashMap<>();
        map.put("login", user.getEmail());
        map.put("password", user.getPassword());
        return map;
    }

    private List<AvailableVisit> loadAndParseAvailableVisits(Subscription subscription, String verificationToken) {
        Map<String, String> requestParameters = prepareRequestParameters(subscription);
        requestParameters.put("verificationToken", verificationToken);
        return anAvailableVisitsParser(timeslotRequest.send(requestParameters)).parse();
    }

    private Map<String, String> prepareRequestParameters(Subscription subscription) {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("serviceId", subscription.getServiceId());
        requestParameters.put("languageId", subscription.getLanguageId());
        requestParameters.put("searchUntilDate", subscription.getSearchUntilDate().format(DD_MM_YYYY));
        return requestParameters;
    }

    private String parseVerificationToken() {
        return SimpleParser.parseVerificationToken(searchRequest.send());
    }

}
