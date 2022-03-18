package uj.jwzp.kpnk.GymApp.exception.event;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventTimeException extends GymAppException {

    public EventTimeException(String eventTitle) {
        super("Event not within the club's opening hours: " + eventTitle);
    }
}