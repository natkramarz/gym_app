package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.Registration;

public record RegistrationCreateRequest(String name, String surname,
                                        int eventId) implements CreateRequest<Registration> {
    @Override
    public Registration asObject() {
        return new Registration(name, surname, eventId);
    }

    @Override
    public Registration asObject(int id) {
        return new Registration(id, name, surname, eventId);
    }
}
