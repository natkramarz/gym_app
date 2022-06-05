package uj.jwzp.kpnk.GymApp.exception.coach;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uj.jwzp.kpnk.GymApp.exception.GymAppException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CoachAlreadyBookedException extends GymAppException {

    public CoachAlreadyBookedException(int coachId) {
        super("Coach with id " + coachId + " is already booked during event hours");
    }
}
