package sp1d.luxnotifier.parser;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class SimpleParser {

    public String parseVerificationToken(String searchPageResponse) {
        return Jsoup.parse(searchPageResponse)
                .select("#advancedResevation > input[name=\"__RequestVerificationToken\"]")
                .attr("value");
    }
}
