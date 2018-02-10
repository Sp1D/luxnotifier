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

import javax.servlet.ServletContext;
import java.util.LinkedHashMap;
import java.util.List;
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
    private ServletContext servletContext;

    @GetMapping("/")
    public ModelAndView subscriptionsPage(ModelAndView mav) {
        if (searchPageParser == null) {
            searchPageParser = new SearchPageParser(searchPage.send());
        }
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, String> services = sortMapValuesByAlphabet(searchPageParser.parseServices());
        Map<String, String> languages = sortMapValuesByAlphabet(searchPageParser.parseLanguages());
        List<Subscription> subscriptions = subscriptionDao.findByUserEmail(userEmail);

        mav.addObject(new Subscription());
        mav.addObject("services", services);
        mav.addObject("languages", languages);
        mav.addObject("subscriptions", subscriptions);
        mav.setViewName("subscriptions");
        return mav;
    }

    @PostMapping("/subscription")
    public String addSubscription(@ModelAttribute Subscription subscription) {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDao.findByEmail(userEmail).isPresent()) {
            User user = User.anUser()
                    .withEmail(userEmail)
                    .withPassword((String) SecurityContextHolder.getContext().getAuthentication().getCredentials())
                    .build();
            userDao.save(user);
        }
        subscription.setUserEmail(userEmail);
        subscription.setServiceName(searchPageParser.parseServices().get(subscription.getServiceId()));
        subscription.setLanguageName(searchPageParser.parseLanguages().get(subscription.getLanguageId()));
        subscriptionDao.save(subscription);
        return "redirect:/";
    }

    @DeleteMapping("/subscription/{serviceId}")
    @ResponseBody
    public String deleteSubscription(@PathVariable int serviceId) {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        subscriptionDao.deleteByUserEmailAndId(userEmail, serviceId);
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
