package uj.jwzp.kpnk.GymApp.service;

import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.exception.club.ClubHasEventException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubOpeningHoursException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.service.ServiceProxy.EventServiceProxyImp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClubService implements ServiceLayer<Club> {

    private final ClubRepository repository;
    private final EventTemplateService eventTemplateService;
    private final EventServiceProxyImp eventService;


    public ClubService(ApplicationContext context) {
        repository = context.getBean(ClubRepository.class);
        eventTemplateService = context.getBean(EventTemplateService.class);
        eventService = context.getBean(EventServiceProxyImp.class);
    }

    @Override
    public List<Club> getAll() {
        return repository.findAll();
    }

    @Override
    public Club get(int id) {
        return repository.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
    }

    @Override
    public Club add(CreateRequest<Club> createRequest) {
        Club club = createRequest.asObject();
        return repository.save(club);
    }

    @Override
    public Club modify(int id, CreateRequest<Club> createRequest) {

        Optional<Club> clubOptional = repository.findById(id);
        if (clubOptional.isEmpty()) throw new ClubNotFoundException(id);

        eventService.getService().eventsByClub(id)
                .forEach(event -> {
                    if (!eventService.getService().isEventBetweenOpeningHours(createRequest.asObject().getWhenOpen(), event.getDay(), event.getStartTime(), event.getDuration())) {
                        throw new ClubOpeningHoursException(id);
                    }
                });

        deleteEventTemplatesByClub(clubOptional.get());

        Club modified = createRequest.asObject(id);
        return repository.save(modified);
    }


    private void deleteEventTemplatesByClub(Club club) {
        List<EventTemplate> eventTemplates = eventTemplateService.eventTemplatesByClub(club.getId());
        List<Integer> idsOfEventTemplatesToDelete = eventTemplates.stream()
                .filter(eventTemplate -> !eventTemplateService.isEventTemplateBetweenOpeningHours(club.getWhenOpen(), eventTemplate.getDay(), eventTemplate.getStartTime(), eventTemplate.getDuration()))
                .map(EventTemplate::getId)
                .collect(Collectors.toList());
        eventTemplateService.deleteEventTemplatesByClub(idsOfEventTemplatesToDelete);
    }

    @Override
    public void delete(int id) {
        Optional<Club> clubRequest = repository.findById(id);
        if (clubRequest.isEmpty()) throw new ClubNotFoundException(id);
        if (!eventService.getService().eventsByClub(id).isEmpty()) throw new ClubHasEventException(id);
        deleteEventTemplatesByClub(clubRequest.get());
        repository.deleteById(id);
    }

    public Page<Club> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }
}
