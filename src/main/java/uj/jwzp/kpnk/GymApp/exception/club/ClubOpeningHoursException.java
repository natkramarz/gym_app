package uj.jwzp.kpnk.GymApp.exception.club;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ClubOpeningHoursException extends GymAppException {

    public ClubOpeningHoursException(int id) {
        super("There are standing out events in club: " + id);
    }
}