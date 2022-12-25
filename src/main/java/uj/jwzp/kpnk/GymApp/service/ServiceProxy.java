package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.ServiceEntity;
import uj.jwzp.kpnk.GymApp.repository.*;

import java.util.List;

@Service
public class ServiceProxy {
    private abstract class ServiceProxyImp<T extends ServiceEntity, T2 extends ServiceLayer<T>> implements ServiceLayer<T> {
        protected T2 service;
        protected final ApplicationContext context;

        @Autowired
        ServiceProxyImp(ApplicationContext context) {
            this.context = context;
        }

        public abstract T2 getService();

        @Override
        public T get(int id) {
            return getService().get(id);
        }

        @Override
        public List<T> getAll() {
            return getService().getAll();
        }

        @Override
        public T add(CreateRequest<T> createRequest) {
            return getService().add(createRequest);
        }

        @Override
        public T modify(int id, CreateRequest<T> createRequest) {
            return getService().modify(id, createRequest);
        }

        @Override
        public void delete(int id) {
            getService().delete(id);
        }
    }

    @Service
    public class CoachServiceProxyImp extends ServiceProxyImp<Coach, CoachService> {

        @Autowired
        public CoachServiceProxyImp(ApplicationContext context) {
            super(context);
        }

        @Override
        public CoachService getService() {
            if (service == null) {
                service = new CoachService(context);
            }
            return service;
        }

    }

    @Service
    public class ClubServiceProxyImp extends ServiceProxyImp<Club, ClubService> {

        @Autowired
        public ClubServiceProxyImp(ApplicationContext context) {
            super(context);
        }

        @Override
        public ClubService getService() {
            if (service == null) {
                service = new ClubService(context);
            }
            return service;
        }

    }

    @Lazy
    @Service
    public class EventServiceProxyImp extends ServiceProxyImp<Event, EventService> {

        @Autowired
        public EventServiceProxyImp(ApplicationContext context) {
            super(context);
        }

        @Override
        public EventService getService() {
            if (service == null) {
                service = new EventService(context);
            }
            return service;
        }

    }
}
