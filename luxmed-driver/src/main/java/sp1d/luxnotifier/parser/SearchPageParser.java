package sp1d.luxnotifier.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SearchPageParser {
    private final Document doc;
    private Map<String, String> servicesMap;
    private Map<String, String> languagesMap;

    public SearchPageParser(String html) {
        doc = Jsoup.parse(html);
    }

    public Map<String, String> parseServices() {
        if (servicesMap == null) {
            servicesMap = parseOptions("#ServiceId > optgroup > option");
        }
        return servicesMap;
    }

    public Map<String, String> parseLanguages() {
        if (languagesMap == null) {
            languagesMap = parseOptions("#LanguageId > option");
        }
        return languagesMap;
    }

    private Map<String, String> parseOptions(String cssSelector) {
        Map<String, String> optionsMap = new HashMap<>();
        for (Element optionElement : doc.select(cssSelector)) {
            String optionId = optionElement.attr("value").trim();
            if (optionId.isEmpty()) {
                continue;
            }
            String optionName = optionElement.text().trim();
            optionsMap.put(optionId, optionName);
        }
        return Collections.unmodifiableMap(optionsMap);
    }

}
