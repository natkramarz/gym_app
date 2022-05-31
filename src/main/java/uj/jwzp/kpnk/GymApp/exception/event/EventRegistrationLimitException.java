package uj.jwzp.kpnk.GymApp.exception.event;

import uj.jwzp.kpnk.GymApp.exception.GymAppException;

public class EventRegistrationLimitException extends GymAppException {

    public EventRegistrationLimitException(int eventId) {
        super("Registration limit reached for the event with id:" + eventId);
    }
}
