package uj.jwzp.kpnk.GymApp.controller.request;

import java.time.LocalDate;

public record EventCreateByTemplateRequest(int templateId, LocalDate eventDate) {
}
