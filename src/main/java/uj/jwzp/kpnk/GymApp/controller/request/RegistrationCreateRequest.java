package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.model.ServiceEntity;

public record RegistrationCreateRequest(String name, String surname, int eventId) implements CreateRequest {
    @Override
    public Registration asObject() {
        return new Registration(name, surname, eventId);
    }

    @Override
    public ServiceEntity asObject(int id) {
        return new Registration(id, name, surname, eventId);
    }
}
