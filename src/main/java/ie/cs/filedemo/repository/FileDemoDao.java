package ie.cs.filedemo.repository;

import ie.cs.filedemo.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
@Slf4j
public class FileDemoDao implements AutoCloseable {
    private Connection connection;
    private final static String sql = "INSERT INTO event (id, duration, type, host, alert)  VALUES (?, ?, ?, ?, ?)";

    @Autowired
    public FileDemoDao(Connection connection) {
        this.connection = connection;
    }

    public void insertEventToDB(final Event event)  {

        log.info("updating event:{} to db",event);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, event.getId());
            statement.setLong(2, event.getDuration());
            statement.setString(3, event.getType());
            statement.setString(4, event.getHost());
            statement.setBoolean(5, event.isAlert());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while inserting event:{} into the table",event);
            //Just throwing runtime exception and exiting. Open to discuss if this needs to continue or to exit.
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
