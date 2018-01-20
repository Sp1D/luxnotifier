package sp1d.luxnotifier;

import org.apache.commons.io.IOUtils;
import sp1d.luxnotifier.parser.AvailableVisitsParserTest;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public static String getResourceAsString(String resourceName) {
        try (InputStream stream = AvailableVisitsParserTest.class.getClassLoader().getResourceAsStream(resourceName)) {
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException ex) {
            return "";
        }
    }
}
