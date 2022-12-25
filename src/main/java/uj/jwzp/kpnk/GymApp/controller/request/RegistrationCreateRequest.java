package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.Registration;

public record RegistrationCreateRequest(int eventId, int gymBroId) implements CreateRequest<Registration> {
    @Override
    public Registration asObject() {
        return new Registration(eventId, gymBroId);
    }

    @Override
    public Registration asObject(int id) {
        return new Registration(id, eventId, gymBroId);
    }
}
