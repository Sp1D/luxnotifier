package sp1d.luxnotifier.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import sp1d.luxnotifier.entity.User;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {

    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .addScripts("sql/schema.sql", "testData.sql")
                .build();
        userDao = new UserDao(new NamedParameterJdbcTemplate(dataSource));
    }

    @Test
    public void savesAndFindsUser() {
        User testUser = User.anUser()
                .withEmail("testuser@test.com")
                .withPassword("")
                .build();
        userDao.save(testUser);
        assertThat(userDao.findByEmail("testuser@test.com").get()).isEqualToComparingFieldByField(testUser);
    }

    @Test
    public void findsAllUsers() {
        List<User> users = userDao.findAll();
        assertThat(users).hasSameElementsAs(allUsers());
    }

    private List<User> allUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.anUser().withEmail("test@test.com").withPassword("test").build());
        users.add(User.anUser().withEmail("test2@test.com").withPassword("test").build());
        return Collections.unmodifiableList(users);
    }
}