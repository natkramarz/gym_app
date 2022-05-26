package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.EventTemplate;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record EventCreateRequest(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {

    public EventTemplate asEvent(int id) {
        return new EventTemplate(id, this.title, this.day, this.time, this.duration, this.clubId, this.coachId);
    }
}
