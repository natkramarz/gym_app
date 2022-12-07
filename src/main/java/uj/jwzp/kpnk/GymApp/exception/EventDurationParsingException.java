package uj.jwzp.kpnk.GymApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Parsing event duration format not possible")
public class EventDurationParsingException extends GymAppException {
    public EventDurationParsingException() {
        super("Parsing event duration format not possible");
    }
}
