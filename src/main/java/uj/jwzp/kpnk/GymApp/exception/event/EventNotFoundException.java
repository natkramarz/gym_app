package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EventNotFoundException extends GymAppException {

    public EventNotFoundException(int id) {
        super("Unknown event id: " + id);
    }
}
