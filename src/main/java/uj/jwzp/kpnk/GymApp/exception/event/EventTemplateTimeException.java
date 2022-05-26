package uj.jwzp.kpnk.GymApp.exception.event;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventTemplateTimeException extends GymAppException {

    public EventTemplateTimeException(String eventTitle) {
        super("Event not within the club's opening hours: " + eventTitle);
    }
}