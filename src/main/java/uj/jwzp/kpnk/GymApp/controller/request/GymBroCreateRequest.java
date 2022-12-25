package uj.jwzp.kpnk.GymApp.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import uj.jwzp.kpnk.GymApp.model.GymBro;

import java.time.LocalDate;

public record GymBroCreateRequest(
        String firstName,
        String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate accountCreatedAt)
        implements CreateRequest<GymBro> {

    @Override
    public GymBro asObject() {
        return new GymBro(firstName, lastName, accountCreatedAt);
    }

    @Override
    public GymBro asObject(int id) {
        return new GymBro(id, firstName, lastName, accountCreatedAt, false);
    }
}
