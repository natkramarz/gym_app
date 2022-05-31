package uj.jwzp.kpnk.GymApp.exception.event;

import uj.jwzp.kpnk.GymApp.exception.GymAppException;

import java.time.LocalDate;

public class EventPastDateException extends GymAppException {
    public EventPastDateException(LocalDate date) {
        super("Date" + date +" of event is in the past" );
    }
}
