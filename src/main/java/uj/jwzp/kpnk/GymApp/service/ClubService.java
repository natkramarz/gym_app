package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.controller.request.ClubCreateRequest;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.exception.club.ClubHasEventException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubOpeningHoursException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClubService implements ServiceLayer {

    private final ClubRepository repository;
    private final EventTemplateService eventTemplateService;
    private final EventService eventService;

    @Autowired
    public ClubService(ClubRepository repository, EventTemplateService eventTemplateService, EventService eventService) {
        this.repository = repository;
        this.eventTemplateService = eventTemplateService;
        this.eventService = eventService;
    }

    @Override
    public Set<Club> getAll() {
        return new HashSet(repository.findAll());
    }

    @Override
    public Club get(int id) {
        return repository.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
    }

    @Override
    public Club add(CreateRequest createRequest) {
        Club club = (Club) createRequest.asObject();
        return repository.save(club);
    }

    @Override
    public Club modify(int id, CreateRequest createRequest) {

        Optional<Club> clubOptional = repository.findById(id);
        if (clubOptional.isEmpty()) throw new ClubNotFoundException(id);

        eventService.eventsByClub(id)
                .forEach(event -> {
                    if (!eventService.isEventBetweenOpeningHours(((ClubCreateRequest)createRequest).whenOpen(), event.getDay(), event.getStartTime(), event.getDuration())) {
                        throw new ClubOpeningHoursException(id);
                    }
                });

        deleteEventTemplatesByClub(clubOptional.get());

        Club modified = (Club) createRequest.asObject(id);
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
        if (!eventService.eventsByClub(id).isEmpty()) throw new ClubHasEventException(id);
        deleteEventTemplatesByClub(clubRequest.get());
        repository.deleteById(id);
    }

    public Page<Club> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }
}
