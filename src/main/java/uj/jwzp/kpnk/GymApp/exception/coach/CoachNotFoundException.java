package uj.jwzp.kpnk.GymApp.exception.coach;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CoachNotFoundException extends GymAppException {

    public CoachNotFoundException(int id) {
        super("Unknown coach id: " + id);
    }
}
