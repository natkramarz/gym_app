package uj.jwzp.kpnk.GymApp.exception.club;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ClubNotFoundException extends GymAppException {
    public ClubNotFoundException(int id) {
        super("Unknown club id: " + id);
    }
}
