package ie.cs.filedemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties (ignoreUnknown = true)
public class Event {
    private String id;
    private State state;
    private String type;
    private String host;
    private Long timestamp;
    private Long duration;
    private boolean alert;
}
