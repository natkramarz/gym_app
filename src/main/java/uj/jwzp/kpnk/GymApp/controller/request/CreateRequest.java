package uj.jwzp.kpnk.GymApp.controller.request;

public interface CreateRequest<T> {
    T asObject();

    T asObject(int id);
}
