package uj.jwzp.kpnk.GymApp.controller.request;

import java.time.LocalDate;

public record CreateEventWithTemplateRequest(int templateId, LocalDate eventDate) {
}
