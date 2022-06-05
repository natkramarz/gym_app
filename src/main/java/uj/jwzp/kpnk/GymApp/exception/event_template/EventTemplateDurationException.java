package uj.jwzp.kpnk.GymApp.exception.event_template;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventTemplateDurationException extends GymAppException {
    public EventTemplateDurationException(String eventTitle) {
        super("Event template duration is too long: " + eventTitle);
    }
}
