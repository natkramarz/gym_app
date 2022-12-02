package uj.jwzp.kpnk.GymApp.controller.request;

import uj.jwzp.kpnk.GymApp.model.ServiceEntity;

public interface CreateRequest {
    ServiceEntity asObject();
    ServiceEntity asObject(int id);
}
