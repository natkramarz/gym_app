package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventNotFoundException;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

@Service
public class EventService {

    private final EventRepository repository;
    private final ClubRepository clubRepository;
    private final CoachRepository coachRepository;

    @Autowired
    public EventService(EventRepository repository, ClubRepository clubRepository, CoachRepository coachRepository) {
        this.repository = repository;
        this.clubRepository = clubRepository;
        this.coachRepository = coachRepository;
    }

    public Event addEvent(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
        if (clubRepository.club(clubId).isEmpty()) throw new ClubNotFoundException(clubId);
        if (coachRepository.coach(coachId).isEmpty()) throw new CoachNotFoundException(coachId);

        return repository.addEvent(title, day, time, duration, clubId, coachId);
    }

    public Set<Event> allEvents() {
        return repository.allEvents();
    }

    public Set<Event> eventsByClub(int clubId) {
        if (clubRepository.club(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        return repository.eventsByClub(clubId);
    }

    public Set<Event> eventsByCoach(int coachId) {
        if (coachRepository.coach(coachId).isEmpty()) throw new CoachNotFoundException(coachId);

        return repository.eventsByCoach(coachId);
    }

    public Event event(int id) {
        return repository.event(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    public void removeEvent(int id) {
        if (repository.event(id).isEmpty()) throw new EventNotFoundException(id);

        repository.removeEvent(id);
    }

    public Event modifyEvent(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
        if (repository.event(id).isEmpty()) throw new EventNotFoundException(id);
        if (coachRepository.coach(coachId).isEmpty()) throw new CoachNotFoundException(id);
        if (clubRepository.club(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        Event modified = new Event(id, title, day, time, duration, clubId, coachId);
        return repository.modifyEvent(id, modified);
    }
}
