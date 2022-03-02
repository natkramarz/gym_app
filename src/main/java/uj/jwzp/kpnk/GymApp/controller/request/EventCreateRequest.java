package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.Event;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record EventCreateRequest(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {

    public Event asEvent(int id) {
        return new Event(id, this.title, this.day, this.time, this.duration, this.clubId, this.coachId);
    }
}
