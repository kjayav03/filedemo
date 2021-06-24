package ie.cs.filedemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.cs.filedemo.model.Event;
import ie.cs.filedemo.model.State;
import ie.cs.filedemo.repository.FileDemoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DemoService {

    private ObjectMapper mapper;
    private FileDemoDao repository;
    private Map<String, Event> eventMap = new HashMap<>();
    private static final Long ALERT_DURATION = 4L;

    @Autowired
    public DemoService(ObjectMapper objectMapper, FileDemoDao repository) {
        this.mapper = objectMapper;
        this.repository = repository;
    }

    public void processEventFile(final String file) {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.lines().forEach(this::populateEventMap);
            log.info("File processing completed for the file : {}", file);
            eventMap.forEach((k, v) -> repository.insertEventToDB(v));
        } catch (IOException e) {
            log.error("Error while reading the input file {}",file, e);
            //Throwing runtime exception for the mo. Customised exception can be thrown here.
            throw new RuntimeException(e.getMessage());
        }
    }

    private void populateEventMap(final String line) {
        try {
            log.debug("processing line :{}", line);
            final Event eventFromFile = mapper.readValue(line, Event.class);
            final Event eventFromMap = eventMap.get(eventFromFile.getId());
            if (null == eventFromMap) {
                eventMap.put(eventFromFile.getId(), eventFromFile);
            } else {
                updateDuration(eventFromFile, eventFromMap);
            }
        } catch (JsonProcessingException e) {
            log.error("Error while processing the line : {}", line);
            // Throwing runtime exception for the mo. Customised exception can be thrown here.
            throw new RuntimeException("Failed processing the line "+ line);
        }
    }


    private void updateDuration(final Event eventFromFile, final Event eventFromMap) {
        if (State.FINISHED.equals(eventFromFile.getState())) {
            eventFromMap.setDuration(eventFromFile.getTimestamp() - eventFromMap.getTimestamp());
        } else {
            eventFromMap.setDuration(eventFromMap.getTimestamp() - eventFromFile.getTimestamp());
        }
        eventFromMap.setAlert(eventFromMap.getDuration() > ALERT_DURATION);

    }

}
