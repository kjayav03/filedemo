package ie.cs.filedemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.cs.filedemo.model.Event;
import ie.cs.filedemo.repository.FileDemoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DemoServiceTest {

    private FileDemoDao fileDemoDao = mock(FileDemoDao.class);
    private DemoService service;

    @BeforeEach
    public void init() {
        service = new DemoService(new ObjectMapper(), fileDemoDao);
    }

    @Test
    public void test_should_populate_alerts_for_longer_duration() {
        final ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        service.processEventFile("src/test/resources/log_event.txt");
        verify(fileDemoDao,times(3)).insertEventToDB(eventArgumentCaptor.capture());
        verify(fileDemoDao,times(3)).insertEventToDB(any(Event.class));
        final List<Event> eventList =  eventArgumentCaptor.getAllValues();
        final Predicate<Event> event1 = event -> event.getId().equals("scsmbstgra") && event.getDuration().equals(5L) && event.isAlert();
        final Predicate<Event> event2 = event -> event.getId().equals("scsmbstgrb") && event.getDuration().equals(3L) && !event.isAlert();
        final Predicate<Event> event3 = event -> event.getId().equals("scsmbstgrc") && event.getDuration().equals(8L) && event.isAlert();

        assertThat(eventList.size()).isEqualTo(3);
        assertThat(eventList.stream().anyMatch(event1)).isTrue();
        assertThat(eventList.stream().anyMatch(event2)).isTrue();
        assertThat(eventList.stream().anyMatch(event3)).isTrue();
    }

    @Test
    public void test_should_exit_on_file_not_found() {
        assertThat(catchThrowable(() -> service.processEventFile("../filenotfound.txt"))).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void test_should_exit_on_invalid_json_formatted_line() {
        assertThat(catchThrowable(() -> service.processEventFile("src/test/resources/invalid_json_line.txt"))).isInstanceOf(RuntimeException.class);
    }


}
