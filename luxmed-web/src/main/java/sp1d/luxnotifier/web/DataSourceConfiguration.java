package sp1d.luxnotifier.web;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@PropertySource("classpath:database.properties")
public class DataSourceConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Autowired
    private Environment environment;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        String user = environment.getProperty("db.user");
        String password = environment.getProperty("db.password");
        String url = environment.getProperty("db.url");
        String driver = environment.getProperty("db.driver");

        LOG.info("Connecting to {}", url);
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);

        return dataSource;
    }

    @Bean(name = "dataSource")
    @Profile("dev")
    public DataSource devDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername("SA");
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
}
