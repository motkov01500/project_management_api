package org.cbg.projectmanagement.project_management.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;

@WebListener
public class FlywayConfiguration implements ServletContextListener {

    @PostConstruct
    public void migrateDatabase() {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource("jdbc:postgresql://db:5432/projectmanagement","postgres","123")
                    .baselineOnMigrate(true)
                    .locations("classpath:db/migration")
                    .load();

            flyway.migrate();
        } catch (Exception e) {
            throw new EJBException("Error migrating database with Flyway", e);
        }
    }
}
