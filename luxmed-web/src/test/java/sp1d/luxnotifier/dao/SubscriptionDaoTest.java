package sp1d.luxnotifier.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import sp1d.luxnotifier.entity.Subscription;

import javax.sql.DataSource;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionDaoTest {
    private SubscriptionDao dao;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .addScripts("sql/schema.sql", "testData.sql")
                .build();
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        dao = new SubscriptionDao(jdbcTemplate);
    }

    @Test
    public void savesAndReadsSubscription() {
        Subscription subscription = aSubscription();
        dao.save(subscription);
        assertThat(dao.findByUserEmail("test@test.com")).contains(subscription);
    }

    @Test
    public void deletesSubscription() {
        assertThat(subscriptionsCount()).isEqualTo(1);
        dao.deleteByUserEmailAndId("test@test.com", 1);
        assertThat(subscriptionsCount()).isZero();
    }

    private int subscriptionsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM SUBSCRIPTION", EmptySqlParameterSource.INSTANCE, Integer.class);
    }

    private Subscription aSubscription() {
        Subscription subscription = new Subscription();
        subscription.setUserEmail("test@test.com");
        subscription.setServiceId("2");
        subscription.setServiceName("Doctor Who");
        subscription.setLanguageId("2");
        subscription.setLanguageName("English");
        subscription.setSearchUntilDate(LocalDate.of(2018, 2, 14));
        return subscription;
    }
}