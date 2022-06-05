package uj.jwzp.kpnk.GymApp.exception.club;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ClubHasEventException extends GymAppException {
    public ClubHasEventException(int id) {
        super("There are events in club: " + id);
    }
}
