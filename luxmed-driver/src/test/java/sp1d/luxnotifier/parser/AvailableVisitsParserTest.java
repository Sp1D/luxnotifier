package sp1d.luxnotifier.parser;

import org.junit.Before;
import org.junit.Test;
import sp1d.luxnotifier.TestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sp1d.luxnotifier.parser.AvailableVisit.anAvailableVisit;

public class AvailableVisitsParserTest {
    private AvailableVisitsParser parser;
    private static String testResponse = TestUtils.getResourceAsString("search_response.html");

    @Before
    public void setUp() throws Exception {
        parser = AvailableVisitsParser.anAvailableVisitsParser(testResponse);
    }

    @Test
    public void parsesAllVisits() {
        List<AvailableVisit> availableVisits = parser.parse();
        assertThat(availableVisits).hasSize(21);
        assertThat(availableVisits).contains(
                anAvailableVisit()
                        .withDoctor("lek. med. CHRAŚCIK-ŚLIZOWSKA PAULA")
                        .withDateTime(LocalDateTime.of(2018, 1, 30, 15, 40))
                        .withClinic("MR Kraków - Wadowicka 8W")
                        .withService("Konsultacja neurologa")
                        .build(),
                anAvailableVisit()
                        .withDoctor("lek. med. CHRAŚCIK-ŚLIZOWSKA PAULA")
                        .withDateTime(LocalDateTime.of(2018, 2, 2, 10, 40))
                        .withClinic("MR Kraków - Wadowicka 8W")
                        .withService("Konsultacja neurologa")
                        .build()
        );
    }
}