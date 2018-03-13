package sp1d.luxnotifier.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sp1d.luxnotifier.parser.AvailableVisit;

import java.util.List;

@Repository
public class NotifiedVisitsDao {
    private final static Logger LOG = LoggerFactory.getLogger(NotifiedVisitsDao.class);
    private NamedParameterJdbcTemplate jdbcTemplate;

    public NotifiedVisitsDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNotifiedVisits(String userEmail, List<AvailableVisit> notifiedVisits) {
        LOG.debug("Saving already notified visits list for user {}", userEmail);
        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[notifiedVisits.size()];
        int i = 0;
        for (AvailableVisit visit : notifiedVisits) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("userEmail", userEmail);
            parameterSource.addValue("doctor", visit.getDoctor());
            parameterSource.addValue("clinic", visit.getClinic());
            parameterSource.addValue("service", visit.getService());
            parameterSource.addValue("dateTime", visit.getDateTime());
            parameterSources[i++] = parameterSource;
        }
        jdbcTemplate.batchUpdate(
                "INSERT INTO notification (user_email, doctor, clinic, service, date_time) VALUES (:userEmail, :doctor, :clinic, :service, :dateTime)",
                parameterSources);
    }

    public List<AvailableVisit> loadNotifiedVisits(String email) {
        LOG.debug("Loading notified visits list for user {}", email);
        return jdbcTemplate.query(
                "SELECT doctor, clinic, service, date_time FROM notification WHERE user_email = :userEmail",
                new MapSqlParameterSource("userEmail", email),
                new BeanPropertyRowMapper<>(AvailableVisit.class)
        );
    }
}
