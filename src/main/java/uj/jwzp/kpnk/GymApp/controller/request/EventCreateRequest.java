package uj.jwzp.kpnk.GymApp.controller.request;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record EventCreateRequest (String title, LocalDate eventDate, LocalTime startTime, Duration duration, int clubId, int coachId, int peopleLimit){
}
