package sp1d.luxnotifier;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LuxCookieJar implements CookieJar {
    private final static Logger LOG = LoggerFactory.getLogger(LuxCookieJar.class);
    private Map<String, Map<String, Cookie>> cookieMap = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        deleteExpiredCookies();
        for (Cookie cookie : cookies) {
            Map<String, Cookie> domainCookies = cookieMap.get(cookie.domain());
            if (domainCookies == null) {
                domainCookies = new HashMap<>();
            }
            LOG.trace("Saving cookie {}", cookie);
            domainCookies.put(cookie.name(), cookie);
            cookieMap.put(cookie.domain(), domainCookies);
        }
    }

    private void deleteExpiredCookies() {
        for (Map<String, Cookie> domainCookies : cookieMap.values()) {
            Iterator<Cookie> cookieIterator = domainCookies.values().iterator();
            while (cookieIterator.hasNext()) {
                if (notExpired(cookieIterator.next())) {
                    continue;
                }
                cookieIterator.remove();
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        Map<String, Cookie> domainCookies = cookieMap.get(url.host());
        if (domainCookies == null) {
            return Collections.emptyList();
        }
        List<Cookie> outputList = new ArrayList<>();
        for (Map.Entry<String, Cookie> cookieEntry : domainCookies.entrySet()) {
            Cookie cookie = cookieEntry.getValue();
            if (notExpired(cookie)) {
                LOG.trace("Loading cookie {}", cookie);
                outputList.add(cookie);
            }
        }
        return outputList;
    }

    private boolean notExpired(Cookie cookie) {
        return System.currentTimeMillis() < cookie.expiresAt();
    }

    public void clear() {
        cookieMap.clear();
    }
}
