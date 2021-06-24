package ie.cs.filedemo.repository;

import ie.cs.filedemo.DemoApplication;
import ie.cs.filedemo.model.Event;
import ie.cs.filedemo.model.State;
import ie.cs.filedemo.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FileDemoDaoTest {

    @Autowired
    private FileDemoDao repository;

    @MockBean
    private DemoService service;

    @MockBean
    private DemoApplication application;

    @Test
    public void should_expect_event_to_be_inserted_into_db() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:eventdb;ifexists=false", "user", "");
        final Statement statement = connection.createStatement();
        statement.execute("DELETE FROM EVENT");
        final Event event = new Event();
        event.setId("1");
        event.setType("testType");
        event.setHost("testHost");
        event.setState(State.STARTED);
        event.setDuration(5L);
        event.setAlert(true);
        repository.insertEventToDB(event);
        final ResultSet result = statement.executeQuery("SELECT * FROM EVENT");
        result.next();
        assertThat(result.getString(1)).isEqualTo("1");
        assertThat(result.getString(2)).isEqualTo("5");
        assertThat(result.getString(3)).isEqualTo("testType");
        assertThat(result.getString(4)).isEqualTo("testHost");
        assertThat(result.getBoolean(5)).isEqualTo(Boolean.TRUE);
    }
}
