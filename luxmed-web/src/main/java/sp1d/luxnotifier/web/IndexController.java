package sp1d.luxnotifier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sp1d.luxnotifier.dao.SubscriptionDao;
import sp1d.luxnotifier.dao.UserDao;
import sp1d.luxnotifier.entity.Subscription;
import sp1d.luxnotifier.entity.User;
import sp1d.luxnotifier.parser.SearchPageParser;
import sp1d.luxnotifier.request.SearchPageRequestSender;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Scope("session")
public class IndexController {

    @Autowired
    private SearchPageRequestSender searchPage;
    @Autowired
    private SubscriptionDao subscriptionDao;
    @Autowired
    private UserDao userDao;
    private SearchPageParser searchPageParser;

    @GetMapping("/")
    public ModelAndView subscriptionsPage(ModelAndView mav) {
        if (searchPageParser == null) {
            searchPageParser = new SearchPageParser(searchPage.send());
        }
        mav.addObject(new Subscription());
        mav.addObject("services", sortMapValuesByAlphabet(searchPageParser.parseServices()));
        mav.addObject("languages", sortMapValuesByAlphabet(searchPageParser.parseLanguages()));
        mav.addObject("subscriptions", subscriptionDao.findByUserEmail(currentUserEmail()));
        mav.setViewName("subscriptions");
        return mav;
    }

    @PostMapping("/subscription")
    public String addSubscription(@ModelAttribute Subscription subscription) {
        createAndSaveUserIfNotExists();

        subscription.setUserEmail(currentUserEmail());
        subscription.setServiceName(searchPageParser.parseServices().get(subscription.getServiceId()));
        subscription.setLanguageName(searchPageParser.parseLanguages().get(subscription.getLanguageId()));
        subscriptionDao.save(subscription);
        return "redirect:/";
    }

    private void createAndSaveUserIfNotExists() {
        String email = currentUserEmail();
        if (!userDao.findByEmail(email).isPresent()) {
            User user = User.anUser()
                    .withEmail(email)
                    .withPassword(currentUserPassword())
                    .build();
            userDao.save(user);
        }
    }

    private String currentUserPassword() {
        return (String)SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    private String currentUserEmail() {
        return (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @DeleteMapping("/subscription/{serviceId}")
    @ResponseBody
    public String deleteSubscription(@PathVariable int serviceId) {
        subscriptionDao.deleteByUserEmailAndId(currentUserEmail(), serviceId);
        return "";
    }

    private Map<String, String> sortMapValuesByAlphabet(Map<String, String> inputMap) {
        return inputMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));
    }

}
