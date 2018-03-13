package sp1d.luxnotifier.web;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
@Component
public class ContextListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(ContextListener.class);

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired(required = false)
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Context is initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Trying to gracefully close everything");
        if (dataSource == null) {
            return;
        }
        try {
            jdbcTemplate.execute("SHUTDOWN", null);
            ((BasicDataSource)dataSource).close();
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                if (driver.getClass().getClassLoader() == cl) {
                    try {
                        DriverManager.deregisterDriver(driver);
                    } catch (SQLException ex) {
                        LOG.debug("Error deregistering JDBC driver {}", driver, ex);
                    }
                }
            }
        } catch (SQLException e) {
            LOG.debug("Something went wrong while closing the BasicDataSource. Let's try one more time...");
            try {
                Thread.sleep(500);
                ((BasicDataSource)dataSource).close();
            } catch (InterruptedException | SQLException ex) {
                LOG.debug("And again something is wrong! Application is giving up.", ex);
            }
        }
    }
}
