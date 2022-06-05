package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EventPastDateException extends GymAppException {
    public EventPastDateException(LocalDate date) {
        super("Date" + date +" of new event is in the past" );
    }
}
