package sp1d.luxnotifier.parser;

import org.springframework.stereotype.Component;

@Component
public class AvailableVisitsParserFactory {
    public AvailableVisitsParser createParser(String htmlContent) {
        return AvailableVisitsParser.anAvailableVisitsParser(htmlContent);
    }
}
