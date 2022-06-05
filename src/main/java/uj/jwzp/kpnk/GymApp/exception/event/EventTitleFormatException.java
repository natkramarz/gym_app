package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EventTitleFormatException extends GymAppException {
    public EventTitleFormatException(String title) {
        super("Event title cannot be empty");
    }
}
