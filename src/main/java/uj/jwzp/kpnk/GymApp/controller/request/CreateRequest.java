package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.ServiceEntity;

public interface CreateRequest<T extends ServiceEntity> {
    T asObject();

    T asObject(int id);
}
