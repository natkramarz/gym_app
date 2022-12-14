package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends GymAppException {
    public EventNotFoundException(int eventId) {
        super("Unknown event id: " + eventId);
    }
}
