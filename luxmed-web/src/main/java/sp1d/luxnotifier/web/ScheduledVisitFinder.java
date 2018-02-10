package sp1d.luxnotifier.web;

import okhttp3.OkHttpClient;
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
    private VisitBooker visitBooker;
    @Autowired
    private LoginRequestSender loginRequest;
    @Autowired
    private SearchPageRequestSender searchRequest;
    @Autowired
    private SearchTimeslotRequestSender timeslotRequest;
    @Autowired
    private OkHttpClient httpClient;


    @Scheduled(cron = "0 * * * * *")
//    @Scheduled(cron = "0 25,55 5-24 * * *")
    public void find() {
        LOG.debug("Scheduled visit search is started");
        for (User user : userDao.findAll()) {
            List<Subscription> subscriptions = subscriptionDao.findByUserEmail(user.getEmail());
            if (CollectionUtils.isEmpty(subscriptions)) {
                LOG.debug("User {} has no subscriptions", user.getEmail());
                continue;
            }
            LOG.debug("Searching for visits subscribed by {}", user.getEmail());
            loginUser(user);
            String verificationToken = parseVerificationToken();
            for (Subscription subscription : subscriptions) {
                LOG.debug("Searching for {}", subscription.getServiceName());
                List<AvailableVisit> availableVisits = loadAndParseAvailableVisits(subscription, verificationToken);
                if (availableVisits.size() > 0) {
                    LOG.info("Subscription for {} found", subscription.getServiceName());
                    visitBooker.bookVisit(subscription);
                    notifier.notifyUser(subscription, availableVisits);
                }
            }
        }
        LOG.debug("Scheduled visit search is stopped");
    }

    private void loginUser(User user) {
        loginRequest.send(user.asMap());
    }

    private List<AvailableVisit> loadAndParseAvailableVisits(Subscription subscription, String verificationToken) {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("serviceId", subscription.getServiceId());
        requestParameters.put("languageId", subscription.getLanguageId());
        requestParameters.put("verificationToken", verificationToken);
        requestParameters.put("searchUntilDate", subscription.getSearchUntilDate().format(DD_MM_YYYY));

        String searchResponse = timeslotRequest.send(requestParameters);
        return anAvailableVisitsParser(searchResponse).parse();
    }

    private String parseVerificationToken() {
        return SimpleParser.parseVerificationToken(searchRequest.send());
    }

}
