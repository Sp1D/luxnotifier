package sp1d.luxnotifier.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import sp1d.luxnotifier.parser.AvailableVisit;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sp1d.luxnotifier.parser.AvailableVisit.anAvailableVisit;

public class NotificationDaoTest {
    public static final String USER_EMAIL = "test@test.com";
    private NotificationDao dao;

    @Before
    public void setUp() throws Exception {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .addScripts("sql/schema.sql", "testData.sql")
                .build();
        dao = new NotificationDao(new NamedParameterJdbcTemplate(dataSource));
    }

    @Test
    public void savesAndLoadsNotifiedVisitList() {
        dao.saveNotifiedVisits(USER_EMAIL, notifications());
        List<AvailableVisit> loadedVisits = dao.loadNotifiedVisits(USER_EMAIL);
        assertThat(loadedVisits).hasSameElementsAs(notifications());
    }

    private List<AvailableVisit> notifications() {
        return Arrays.asList(
                anAvailableVisit()
                        .withClinic("clinic")
                        .withDateTime(LocalDateTime.of(2018, 2, 14, 7, 40))
                        .withDoctor("doctor")
                        .withService("service")
                        .build(),
                anAvailableVisit()
                        .withClinic("clinic1")
                        .withDateTime(LocalDateTime.of(2018, 2, 15, 9, 50))
                        .withDoctor("doctor1")
                        .withService("service1")
                        .build()
        );
    }
}