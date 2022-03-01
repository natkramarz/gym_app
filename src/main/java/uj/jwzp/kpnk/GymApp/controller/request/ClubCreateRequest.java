package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.OpeningHours;

import java.time.DayOfWeek;
import java.util.Map;

public record ClubCreateRequest(String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
}
