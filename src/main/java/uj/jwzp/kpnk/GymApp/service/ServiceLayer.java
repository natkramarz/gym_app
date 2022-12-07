package uj.jwzp.kpnk.GymApp.service;

import uj.jwzp.kpnk.GymApp.controller.request.ClubCreateRequest;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.ServiceEntity;

import java.util.List;
import java.util.Set;

public interface ServiceLayer<T extends ServiceEntity > {
    public T get(int id);

    public List<T> getAll();

    public T add(CreateRequest<T> createRequest);

    public T modify(int id, CreateRequest<T> createRequest);

    public void delete(int id);

}
