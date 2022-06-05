package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

import java.time.DayOfWeek;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EventTemplateDayOfWeekMismatchException extends GymAppException {

    public EventTemplateDayOfWeekMismatchException(DayOfWeek templateDay, DayOfWeek eventDay) {
        super("template day of week: " + templateDay + " is not equal day of event: " + eventDay);
    }
}
