package uj.jwzp.kpnk.GymApp.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record Event(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
}
