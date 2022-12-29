package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.Address;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;

import java.time.DayOfWeek;
import java.util.Map;

public record ClubCreateRequest(String name,
                                Map<DayOfWeek, OpeningHours> whenOpen, Address address) implements CreateRequest<Club> {
    @Override
    public Club asObject() {
        return new Club(name, address, whenOpen);
    }

    @Override
    public Club asObject(int id) {
        return new Club(id, name, address,  whenOpen);
    }

}
