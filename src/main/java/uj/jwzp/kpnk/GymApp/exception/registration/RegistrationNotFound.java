package uj.jwzp.kpnk.GymApp.exception.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RegistrationNotFound extends GymAppException {
    public RegistrationNotFound(int id) {
        super("Unknown registration id " + id);
    }
}
