package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.exception.club.ClubHasEventException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubOpeningHoursException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ClubService {

    private final ClubRepository repository;
    private final EventService eventService;

    @Autowired
    public ClubService(ClubRepository repository, EventService eventService) {
        this.repository = repository;
        this.eventService = eventService;
    }

    public List<Club> allClubs() {
        return repository.findAll();
    }

    public Club club(int id) {
        return repository.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
    }

    public Club addClub(String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        Club club = new Club(name, address, whenOpen);
        return repository.save(club);
    }

    public Club modifyClub(int id, String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        if (repository.findById(id).isEmpty()) throw new ClubNotFoundException(id);
        List<Event> events = eventService.eventsByClub(id);
        for (Event event: events) {
            if (!eventService.isEventTimeBetweenClubOpeningHours(whenOpen, event.getDay(), event.getTime(), event.getDuration()))
                throw new ClubOpeningHoursException(id);
        }
        Club modified = new Club(id, name, address, whenOpen);
        return repository.save(modified);
    }

    public void removeClub(int id) {
        if (repository.findById(id).isEmpty()) throw new ClubNotFoundException(id);
        if (!eventService.eventsByClub(id).isEmpty()) throw new ClubHasEventException(id);

        repository.deleteById(id);
    }
}
