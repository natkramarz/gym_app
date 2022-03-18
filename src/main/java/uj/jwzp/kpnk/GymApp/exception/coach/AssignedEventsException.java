package uj.jwzp.kpnk.GymApp.exception.coach;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AssignedEventsException extends GymAppException {

    public AssignedEventsException(int id) {
        super("There are events assigned to the coach with id: " + id);
    }
}