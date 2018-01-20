package sp1d.luxnotifier.parser;

import org.jsoup.Jsoup;

public class SimpleParser {

    public static String parseVerificationToken(String searchPageResponse) {
        return Jsoup.parse(searchPageResponse)
                .select("#advancedResevation > input[name=\"__RequestVerificationToken\"]")
                .attr("value");
    }
}
