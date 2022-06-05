package uj.jwzp.kpnk.GymApp.exception.event_template;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EventTemplateNotFoundException extends GymAppException {
    public EventTemplateNotFoundException(int id) {
        super("Unknown event id: " + id);
    }
}
