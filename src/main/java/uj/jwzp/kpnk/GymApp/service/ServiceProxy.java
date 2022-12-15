package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    private abstract static class ServiceProxyImp<T extends ServiceEntity, T2 extends ServiceLayer<T>> implements ServiceLayer<T> {
        protected T2 service;

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
        private final CoachRepository repository;
        private final EventTemplateService eventTemplateService;
        private final EventServiceProxyImp eventService;

        @Autowired
        public CoachServiceProxyImp(CoachRepository coachRepository, EventTemplateService eventTemplateService, @Lazy EventServiceProxyImp eventService) {
            this.repository = coachRepository;
            this.eventTemplateService = eventTemplateService;
            this.eventService = eventService;
        }

        @Override
        public CoachService getService() {
            if (service == null) {
                service = new CoachService(repository, eventTemplateService, eventService);
            }
            return service;
        }

    }

    @Service
    public class ClubServiceProxyImp extends ServiceProxyImp<Club, ClubService> {
        private final ClubRepository repository;
        private final EventTemplateService eventTemplateService;
        private final EventServiceProxyImp eventService;

        @Autowired
        public ClubServiceProxyImp(ClubRepository repository, EventTemplateService eventTemplateService, @Lazy EventServiceProxyImp eventService) {
            this.repository = repository;
            this.eventTemplateService = eventTemplateService;
            this.eventService = eventService;
        }

        @Override
        public ClubService getService() {
            if (service == null) {
                service = new ClubService(repository, eventTemplateService, eventService);
            }
            return service;
        }

    }

    @Lazy
    @Service
    public class EventServiceProxyImp extends ServiceProxyImp<Event, EventService> {
        private final EventRepository repository;
        private final ClubServiceProxyImp clubService;
        private final CoachServiceProxyImp coachService;
        private final EventTemplateRepository eventTemplateRepository;
        private final RegistrationRepository registrationRepository;

        @Autowired
        public EventServiceProxyImp(EventRepository repository, ClubServiceProxyImp clubService, CoachServiceProxyImp coachService, EventTemplateRepository eventTemplateRepository, RegistrationRepository registrationRepository) {
            this.repository = repository;
            this.clubService = clubService;
            this.coachService = coachService;
            this.eventTemplateRepository = eventTemplateRepository;
            this.registrationRepository = registrationRepository;
        }

        @Override
        public EventService getService() {
            if (service == null) {
                service = new EventService(repository, clubService, coachService, eventTemplateRepository, registrationRepository);
            }
            return service;
        }

    }
}
