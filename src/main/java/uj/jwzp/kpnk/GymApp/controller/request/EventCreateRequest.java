package uj.jwzp.kpnk.GymApp.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uj.jwzp.kpnk.GymApp.model.Event;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record EventCreateRequest(
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate eventDate,
        LocalTime startTime,
        @JsonDeserialize(using = EventDurationDeserializer.class) Duration duration,
        int clubId,
        int coachId,
        int peopleLimit) implements CreateRequest<Event> {
    @Override
    public Event asObject() {
        return new Event(title, eventDate, duration, startTime, clubId, coachId, peopleLimit);
    }

    @Override
    public Event asObject(int id) {
        return new Event(id, title, eventDate, duration, startTime, clubId, coachId, peopleLimit);
    }
}
