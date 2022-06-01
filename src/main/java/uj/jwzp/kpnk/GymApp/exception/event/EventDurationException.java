package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EventDurationException extends GymAppException {
    public EventDurationException() {
        super("Event duration cannot be longer than 24 hours");
    }
}
