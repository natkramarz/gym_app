package uj.jwzp.kpnk.GymApp.exception.gym_bro;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GymBroNotFoundException extends GymAppException {
    public GymBroNotFoundException(int id) {
        super("Unknown gym bro id: " + id);
    }
}
