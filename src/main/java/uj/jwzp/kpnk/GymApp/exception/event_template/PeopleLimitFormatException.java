package uj.jwzp.kpnk.GymApp.exception.event_template;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PeopleLimitFormatException extends GymAppException {

    public PeopleLimitFormatException(int peopleLimit) {
        super("People limit is negative: " + peopleLimit);
    }
}
