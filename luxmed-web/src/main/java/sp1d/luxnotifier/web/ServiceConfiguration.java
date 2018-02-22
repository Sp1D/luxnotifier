package sp1d.luxnotifier.web;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@EnableScheduling
@Import(EmailConfiguration.class)
public class ServiceConfiguration {


/*@Bean
    @Profile("dev")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("sql/schema.sql")
                .build();
    }*/

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
//        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUsername("SA");
//        dataSource.setUser("SA");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:hsqldb:file:db/sqlnotifier_db");
        dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        initDatabase(dataSource);

        return dataSource;
    }

    private void initDatabase(DataSource dataSource) {
        int existingTables = 0;

        try (Statement st = dataSource.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='PUBLIC'")) {

            rs.next();
            existingTables = rs.getInt(1);
        } catch (SQLException ex) {
        }

        if (existingTables == 0) {
            ResourceDatabasePopulator dbPopulator = new ResourceDatabasePopulator(new ClassPathResource("sql/schema.sql"));
            dbPopulator.setIgnoreFailedDrops(true);
            DatabasePopulatorUtils.execute(dbPopulator, dataSource);
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public SimpleJdbcInsert simpleJdbcInsert() {
        return new SimpleJdbcInsert(jdbcTemplate());
    }
}
