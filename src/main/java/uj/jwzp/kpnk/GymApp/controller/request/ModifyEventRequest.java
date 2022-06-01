package uj.jwzp.kpnk.GymApp.controller.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record ModifyEventRequest(LocalDate date, LocalTime time) {
}
