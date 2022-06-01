package uj.jwzp.kpnk.GymApp.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record EventTemplateCreateRequest(
        String title,
        DayOfWeek day,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") LocalTime time,
        @JsonDeserialize(using = DurationDeserializer.class) Duration duration,
        int clubId,
        int coachId,
        int peopleLimit
    ) {

}
