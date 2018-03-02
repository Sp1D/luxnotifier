package sp1d.luxnotifier.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import sp1d.luxnotifier.entity.User;

import java.util.List;
import java.util.Optional;

@Component
public class UserDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("user");
    }

    public int save(User user) {
        LOG.debug("Saving user {}", user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        return simpleJdbcInsert.execute(parameterSource);
    }

    public Optional<User> findByEmail(String email) {
        LOG.debug("Finding user {}", email);
        User user = null;
        RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);
        try {
            user = jdbcTemplate.queryForObject("SELECT EMAIL, PASSWORD FROM user WHERE EMAIL = ?", userRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("No such user with email {}", email);
        }
        return Optional.ofNullable(user);
    }

    public List<User> findAll() {
        LOG.debug("Finding all users");
        return jdbcTemplate.query("SELECT EMAIL, PASSWORD FROM user", new BeanPropertyRowMapper<>(User.class));
    }

}
