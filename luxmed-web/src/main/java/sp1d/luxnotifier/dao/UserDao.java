package sp1d.luxnotifier.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import sp1d.luxnotifier.entity.User;

import java.util.List;
import java.util.Optional;

@Component
public class UserDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);
    private NamedParameterJdbcTemplate jdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(User user) {
        LOG.debug("Saving user {}", user);
        return jdbcTemplate.update("INSERT INTO users (email, password) VALUES (:email, :password)", new BeanPropertySqlParameterSource(user));
    }

    public Optional<User> findByEmail(String email) {
        LOG.debug("Finding user {}", email);
        User user = null;
        try {
            user = jdbcTemplate.queryForObject("SELECT EMAIL, PASSWORD FROM users WHERE EMAIL = :email",
                    new MapSqlParameterSource("email", email),
                    new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("No such user with email {}", email);
        }
        return Optional.ofNullable(user);
    }

    public List<User> findAll() {
        LOG.debug("Finding all users");
        return jdbcTemplate.query("SELECT EMAIL, PASSWORD FROM users", new BeanPropertyRowMapper<>(User.class));
    }

}
