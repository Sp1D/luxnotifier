package sp1d.luxnotifier.parser;

import org.junit.Test;
import sp1d.luxnotifier.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;


public class SearchPageParserTest {
    private final String sourceHtml = TestUtils.getResourceAsString("search_response.html");
    private final SearchPageParser parser = new SearchPageParser(sourceHtml);

    @Test
    public void parsesServices() {
        assertThat(parser.parseServices().get("5857")).isEqualTo("Audiometr standardowy");
    }

    @Test
    public void parsesLanguages() {
        assertThat(parser.parseLanguages().get("11")).isEqualTo("English");
    }
}