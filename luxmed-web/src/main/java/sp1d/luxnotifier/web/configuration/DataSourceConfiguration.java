package sp1d.luxnotifier.web.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DataSourceConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername("SA");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:hsqldb:file:/mnt/data/luxnotifier_db");
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
            LOG.info("Initializing DB schema");
            ResourceDatabasePopulator dbPopulator = new ResourceDatabasePopulator(new ClassPathResource("sql/schema.sql"));
            dbPopulator.setIgnoreFailedDrops(true);
            DatabasePopulatorUtils.execute(dbPopulator, dataSource);
        }
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource());
        return manager;
    }
}
