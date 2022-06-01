package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EventFullyBookedException extends GymAppException {
    public EventFullyBookedException(int eventId) {
        super("Registration limit reached for the event with id:" + eventId);
    }
}
