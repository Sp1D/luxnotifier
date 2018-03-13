package sp1d.luxnotifier.dao;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import sp1d.luxnotifier.parser.AvailableVisit;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class NotifiedVisitsDaoTest {
    private static final String USER_EMAIL = "test@test.com";
    private NotifiedVisitsDao dao;

    @Before
    public void setUp() throws Exception {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .addScripts("sql/schema.sql", "testData.sql")
                .build();
        dao = new NotifiedVisitsDao(new NamedParameterJdbcTemplate(dataSource));
    }

    @Test
    public void savesAndLoadsNotifiedVisitList() {
        dao.saveNotifiedVisits(USER_EMAIL, notifications());
        List<AvailableVisit> loadedVisits = dao.loadNotifiedVisits(USER_EMAIL);
        Assertions.assertThat(loadedVisits).hasSameElementsAs(notifications());
    }

    private List<AvailableVisit> notifications() {
        return Arrays.asList(
                AvailableVisit.anAvailableVisit()
                        .withClinic("clinic")
                        .withDateTime(LocalDateTime.of(2018, 2, 14, 7, 40))
                        .withDoctor("doctor")
                        .withService("service")
                        .build(),
                AvailableVisit.anAvailableVisit()
                        .withClinic("clinic1")
                        .withDateTime(LocalDateTime.of(2018, 2, 15, 9, 50))
                        .withDoctor("doctor1")
                        .withService("service1")
                        .build()
        );
    }
}