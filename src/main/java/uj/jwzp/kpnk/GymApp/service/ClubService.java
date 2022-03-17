package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubHasEventException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Optional;
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

    public Set<Club> allClubs() {
        return repository.allClubs();
    }

    public Club club(int id) {
        return repository.club(id).orElseThrow(() -> new ClubNotFoundException(id));
    }

    public Club addClub(String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        return repository.addClub(name, address, whenOpen);
    }

    public Club modifyClub(int id, String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        if (repository.club(id).isEmpty()) throw new ClubNotFoundException(id);

        Club modified = new Club(id, name, address, whenOpen);
        return repository.modifyClub(id, modified);
    }

    public void removeClub(int id) {
        if (repository.club(id).isEmpty()) throw new ClubNotFoundException(id);
        if (!eventService.eventsByClub(id).isEmpty()) throw new ClubHasEventException(id);

        repository.removeClub(id);
    }
}
