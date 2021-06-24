package ie.cs.filedemo;

import ie.cs.filedemo.service.DemoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;


public class DemoApplicationTest {

    private DemoApplication demoApplication;
    private DemoService service = mock(DemoService.class);

    @BeforeEach
    public void init() {
        demoApplication = new DemoApplication();
        ReflectionTestUtils.setField(demoApplication, "service", service);
    }

    @Test
    public void should_expect_service_to_be_called_once_for_non_empty_file() throws IOException {
        demoApplication.run("/sometextfile");
        verify(service,times(1)).processEventFile("/sometextfile");
    }

    @Test
    public void should_expect_run_time_exception_for_null_argument() throws IOException {
        assertThat(catchThrowable(() -> demoApplication.run(null))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_expect_run_time_exception_for_empty_argument() throws IOException {
        assertThat(catchThrowable(() -> demoApplication.run(""))).isInstanceOf(IllegalArgumentException.class);
    }
}
