package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.model.ServiceEntity;

import java.time.DayOfWeek;
import java.util.Map;

public record ClubCreateRequest(String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) implements CreateRequest {
    @Override
    public Club asObject(){
        return new Club(name, address, whenOpen);
    }
    @Override
    public Club asObject(int id){
        return new Club(id, name, address, whenOpen);
    }

}
