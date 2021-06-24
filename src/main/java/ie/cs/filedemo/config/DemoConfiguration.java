package ie.cs.filedemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
@Slf4j
public class DemoConfiguration {

    @Value("${filedemo.db.driver:org.hsqldb.jdbc.JDBCDriver}")
    private String driver;
    @Value("${filedemo.db.url:jdbc:hsqldb:file:eventdb;ifexists=false}")
    private String url;
    @Value("${filedemo.db.user:user}")
    private String user;
    @Value("${filedemo.db.password:}")
    private String password;
    @Value("${filedemo.db.ddl:CREATE TABLE IF NOT EXISTS event (id VARCHAR(20), duration INTEGER, type VARCHAR(20), host VARCHAR(50), alert BOOLEAN)}")
    private String ddl;

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Connection connection() {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.createStatement().execute(ddl);
            return connection;
        } catch (Exception e) {
            log.error("Err initialising HSQL JDBCDriver");
            throw new BeanCreationException("Error while creating db connection");
        }
    }
}
