package uj.jwzp.kpnk.GymApp.controller.request;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record EventCreateRequest (String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, LocalDate eventDate){
}
