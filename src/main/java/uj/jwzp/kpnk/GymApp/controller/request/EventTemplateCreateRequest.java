package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.EventTemplate;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record EventTemplateCreateRequest(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, int peopleLimit) {

}
