package uj.jwzp.kpnk.GymApp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.exception.coach.AssignedEventsException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;
import uj.jwzp.kpnk.GymApp.service.ServiceProxy.EventServiceProxyImp;

import java.util.List;

@Service
public class CoachService implements ServiceLayer<Coach> {

    private final CoachRepository repository;
    private final EventTemplateService eventTemplateService;
    private final EventServiceProxyImp eventService;

    public CoachService(CoachRepository repository, EventTemplateService eventTemplateService, EventServiceProxyImp eventService) {
        this.repository = repository;
        this.eventTemplateService = eventTemplateService;
        this.eventService = eventService;
    }


    @Override
    public List<Coach> getAll() {
        return repository.findAll();
    }

    @Override
    public Coach get(int id) {
        return repository.findById(id).orElseThrow(() -> new CoachNotFoundException(id));
    }

    @Override
    public Coach add(CreateRequest<Coach> createRequest) {
        Coach coach = createRequest.asObject();
        return repository.save(coach);
    }

    @Override
    public Coach modify(int id, CreateRequest<Coach> createRequest) {
        if (repository.findById(id).isEmpty()) throw new CoachNotFoundException(id);
        Coach modified = createRequest.asObject(id);
        return repository.save(modified);
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isEmpty()) throw new CoachNotFoundException(id);
        if (!eventService.getService().eventsByCoach(id).isEmpty()) throw new AssignedEventsException(id);
        eventTemplateService.deleteEventTemplatesByCoach(id);
        repository.deleteById(id);
    }

    public Page<Coach> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }


}
