package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventDurationException extends GymAppException {

    public EventDurationException(String eventTitle) {
        super("Event duration too long: " + eventTitle);
    }
}
