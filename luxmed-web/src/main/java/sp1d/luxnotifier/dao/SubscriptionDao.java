package sp1d.luxnotifier.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import sp1d.luxnotifier.entity.Subscription;

import java.sql.Types;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class SubscriptionDao {
    private static final String SELECT_SUBSCRIPTION_BY_EMAIL =
            "SELECT USER_EMAIL, SERVICE_ID, SERVICE_NAME, LANGUAGE_ID, LANGUAGE_NAME, SEARCH_UNTIL_DATE FROM subscription WHERE USER_EMAIL = :email";
    private static final String USER_EMAIL = "user_email";
    private static final String SERVICE_ID = "service_id";
    private static final String SERVICE_NAME = "service_name";
    private static final String LANGUAGE_ID = "language_id";
    private static final String SEARCH_UNTIL_DATE = "search_until_date";
    private static final String LANGUAGE_NAME = "language_name";
    final private NamedParameterJdbcTemplate jdbcTemplate;

    public SubscriptionDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Subscription subscription) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(USER_EMAIL, subscription.getUserEmail());
        parameters.addValue(SERVICE_ID, subscription.getServiceId());
        parameters.addValue(SERVICE_NAME, subscription.getServiceName());
        parameters.addValue(LANGUAGE_ID, subscription.getLanguageId());
        parameters.addValue(LANGUAGE_NAME, subscription.getLanguageName());
        parameters.addValue(SEARCH_UNTIL_DATE, LocalDateTime.of(subscription.getSearchUntilDate(), LocalTime.MIDNIGHT), Types.DATE);
        jdbcTemplate.update("INSERT INTO subscription (user_email, service_id, service_name, language_id, language_name, search_until_date) " +
                "VALUES (:user_email, :service_id, :service_name, :language_id, :language_name, :search_until_date)", parameters);
    }

    public List<Subscription> findByUserEmail(String email) {
        return jdbcTemplate.query(SELECT_SUBSCRIPTION_BY_EMAIL, new MapSqlParameterSource("email", email),
                (rs, rowNum) -> Subscription.aSubscription()
                        .withUserEmail(rs.getString(USER_EMAIL))
                        .withServiceId(rs.getString(SERVICE_ID))
                        .withServiceName(rs.getString(SERVICE_NAME))
                        .withLanguageId(rs.getString(LANGUAGE_ID))
                        .withLanguageName(rs.getString(LANGUAGE_NAME))
                        .withSearchUntilDate(rs.getDate(SEARCH_UNTIL_DATE).toLocalDate())
                        .build());
    }

    public int deleteByUserEmailAndId(String email, int serviceId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", email);
        parameters.addValue("serviceId", serviceId);
        return jdbcTemplate.update("DELETE FROM subscription WHERE USER_EMAIL = :email AND SERVICE_ID = :serviceId", parameters);
    }
}
