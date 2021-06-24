package ie.cs.filedemo;

import ie.cs.filedemo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private DemoService service;

	public static void main(String[] args) {
        log.info("Starting aplication");
		SpringApplication.run(DemoApplication.class, args);
		log.info("Started");
	}

	public void run(String... args) throws IOException {
	    if(null == args || isEmpty(args[0])) {
	        // a bad request exception to be thrown here. Illegal arugument for the mo.
	        throw new IllegalArgumentException("File path is empty");
        }
		log.info("Processing the input file: {}",args[0]);
	    final String filePath = args[0];
	    service.processEventFile(filePath);
    }
}
