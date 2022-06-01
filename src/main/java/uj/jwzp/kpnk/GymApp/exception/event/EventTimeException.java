package uj.jwzp.kpnk.GymApp.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EventTimeException extends GymAppException {
    public EventTimeException(int clubId) {
        super("New event time not within opening hours of club:" + clubId);
    }
}
