package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.Coach;

public record CoachCreateRequest(String firstName, String lastName, int yearOfBirth) implements CreateRequest<Coach> {
    @Override
    public Coach asObject() {
        return new Coach(firstName, lastName, yearOfBirth);
    }

    @Override
    public Coach asObject(int id) {
        return new Coach(id, firstName, lastName, yearOfBirth);
    }
}
