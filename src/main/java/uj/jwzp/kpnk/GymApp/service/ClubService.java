package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.exception.club.ClubHasEventException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubOpeningHoursException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClubService {

    private final ClubRepository repository;
    private final EventTemplateService eventTemplateService;
    private final EventService eventService;

    @Autowired
    public ClubService(ClubRepository repository, EventTemplateService eventTemplateService, EventService eventService) {
        this.repository = repository;
        this.eventTemplateService = eventTemplateService;
        this.eventService = eventService;
    }

    public Set<Club> allClubs() {
        return new HashSet(repository.findAll());
    }

    public Club club(int id) {
        return repository.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
    }

    public Club addClub(String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        Club club = new Club(name, address, whenOpen);
        return repository.save(club);
    }

    private void deleteEventTemplatesByClub(Club club) {
        List<EventTemplate> eventTemplates = eventTemplateService.eventTemplatesByClub(club.getId());
        List<Integer> idsOfEventTemplatesToDelete = eventTemplates.stream()
                .filter(eventTemplate -> !eventTemplateService.isEventTemplateBetweenOpeningHours(club.getWhenOpen(), eventTemplate.getDay(), eventTemplate.getStartTime(), eventTemplate.getDuration()))
                .map(EventTemplate::getId)
                .collect(Collectors.toList());
        eventTemplateService.deleteEventTemplatesByClub(idsOfEventTemplatesToDelete);
    }

    public Club modifyClub(int id, String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        Optional<Club> clubOptional = repository.findById(id);
        if (clubOptional.isEmpty()) throw new ClubNotFoundException(id);

        eventService.eventsByClub(id)
            .forEach(event -> {
                if (!eventService.isEventBetweenOpeningHours(whenOpen, event.getDay(), event.getStartTime(), event.getDuration())) {
                    throw new ClubOpeningHoursException(id);
                }
            });

        deleteEventTemplatesByClub(clubOptional.get());

        Club modified = new Club(id, name, address, whenOpen);
        return repository.save(modified);
    }

    public void deleteClub(int id) {
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
