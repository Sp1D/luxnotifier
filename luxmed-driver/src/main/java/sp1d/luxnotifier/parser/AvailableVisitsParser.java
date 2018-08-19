package sp1d.luxnotifier.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvailableVisitsParser {
    private static final Logger LOG = LoggerFactory.getLogger(AvailableVisitsParser.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final Document doc;

    private AvailableVisitsParser(Document doc) {
        this.doc = doc;
    }

    public static AvailableVisitsParser anAvailableVisitsParser(String html) {
        Document doc = Jsoup.parse(html);
        doc.charset(Charset.forName("UTF-8"));
        return new AvailableVisitsParser(doc);
    }

    public List<AvailableVisit> parse() {
        return parsePlease();
    }

    private List<AvailableVisit> parsePlease() {
        List<AvailableVisit> availableVisits = new ArrayList<>();
        Elements dayElements = doc.select("#content > div.full > ul > li");
        for (Element dayElement : dayElements) {
            LocalDate date = parseDate(dayElement);
            Elements visitElements = dayElement.select("div.content > table > tbody > tr");
            for (Element visitElement : visitElements) {
                try {
                    AvailableVisit availableVisit = AvailableVisit.anAvailableVisit()
                            .withDoctor(getElementText(visitElement, "td:nth-child(2) > div:nth-child(1)"))
                            .withDateTime(parseAndConstructVisitDateTime(date, visitElement))
                            .withClinic(getElementText(visitElement, "td:nth-child(2) > div:nth-child(3)"))
                            .withService(getElementText(visitElement, "td:nth-child(2) > div:nth-child(2)"))
                            .build();
                    availableVisits.add(availableVisit);
                } catch (Exception e) {
                    LOG.info("Error while parsing timeslot [{}]", visitElement.text());
                }
            }
        }
        return availableVisits;
    }

    private String getElementText(Element element, String selector) {
        return element.select(selector).text().trim();
    }

    private LocalDate parseDate(Element dayElement) {
        String dateString = dayElement.select("div.title").text().split(",")[1].trim();
        return LocalDate.parse(dateString, DATE_FORMAT);
    }

    private LocalDateTime parseAndConstructVisitDateTime(LocalDate date, Element visitElement) {
        String timeElementText = visitElement.select("td.hours > div > a").text();
        int colonPosition = timeElementText.indexOf(":");
        String timeString = timeElementText.substring(colonPosition - 2, colonPosition + 3).trim();
        return LocalDateTime.of(date, LocalTime.parse(timeString, TIME_FORMAT));
    }
}
