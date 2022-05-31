package uj.jwzp.kpnk.GymApp.exception.event;

import uj.jwzp.kpnk.GymApp.exception.GymAppException;

public class EventNotFoundException extends GymAppException {

    public EventNotFoundException(int eventId) {
        super("Unknown event id: " + eventId);
    }
}
