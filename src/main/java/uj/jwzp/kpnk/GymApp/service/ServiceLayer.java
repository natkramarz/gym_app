package uj.jwzp.kpnk.GymApp.service;

import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.model.DomainObject;

import java.util.List;

public interface ServiceLayer<T extends DomainObject> {
    T get(int id);

    List<T> getAll();

    T add(CreateRequest<T> createRequest);

    T modify(int id, CreateRequest<T> createRequest);

    void delete(int id);

}
