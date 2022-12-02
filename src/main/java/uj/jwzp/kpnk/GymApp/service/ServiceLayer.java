package uj.jwzp.kpnk.GymApp.service;

import uj.jwzp.kpnk.GymApp.controller.request.ClubCreateRequest;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.ServiceEntity;

import java.util.Set;

public interface ServiceLayer {
    public Set<? extends ServiceEntity> getAll();
    public ServiceEntity get(int id);
    public ServiceEntity add(CreateRequest createRequest);
    public ServiceEntity modify(int id, CreateRequest createRequest);

    public void delete(int id);

}