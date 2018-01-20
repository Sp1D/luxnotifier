package sp1d.luxnotifier.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AvailableVisitsParser {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final Document doc;
    private List<AvailableVisit> parsedVisits;

    private AvailableVisitsParser(Document doc) {
        this.doc = doc;
    }

    public static AvailableVisitsParser createParser(String html) {
        return new AvailableVisitsParser(Jsoup.parse(html));
    }

    public List<AvailableVisit> parse() {
        if (parsedVisits == null) {
            parsedVisits = parsePlease();
        }
        return parsedVisits;
    }

    private List<AvailableVisit> parsePlease() {
        List<AvailableVisit> availableVisits = new ArrayList<>();
        Elements dayElements = doc.select("#content > div.full > ul > li");
        for (Element dayElement : dayElements) {
            LocalDate date = parseDate(dayElement);
            Elements visitElements = dayElement.select("div.content > table > tbody > tr");
            for (Element visitElement : visitElements) {
                AvailableVisit availableVisit = AvailableVisit.builder()
                        .doctor(getElementText(visitElement, "td:nth-child(2) > div:nth-child(1)"))
                        .dateTime(parseAndConstructVisitDateTime(date, visitElement))
                        .clinic(getElementText(visitElement, "td:nth-child(2) > div:nth-child(3)"))
                        .service(getElementText(visitElement, "td:nth-child(2) > div:nth-child(2)"))
                        .build();
                availableVisits.add(availableVisit);
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
        String timeString = visitElement.select("td.hours > div > a").text().replace("Book", "").trim();
        return LocalDateTime.of(date, LocalTime.parse(timeString, TIME_FORMAT));
    }
}
