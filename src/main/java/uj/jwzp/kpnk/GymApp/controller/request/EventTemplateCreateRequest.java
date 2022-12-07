package uj.jwzp.kpnk.GymApp.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record EventTemplateCreateRequest(
        String title,
        DayOfWeek day,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") LocalTime time,
        @JsonDeserialize(using = EventDurationDeserializer.class) Duration duration,
        int clubId,
        int coachId,
        int peopleLimit
) implements CreateRequest<EventTemplate> {

    @Override
    public EventTemplate asObject() {
        return new EventTemplate(title, day, time, duration, clubId, coachId, peopleLimit);
    }

    @Override
    public EventTemplate asObject(int id) {
        return new EventTemplate(id, title, day, time, duration, clubId, coachId, peopleLimit);
    }
}
