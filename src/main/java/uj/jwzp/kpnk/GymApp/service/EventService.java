package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Service
public class EventService {

    private EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event addEvent(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
        return repository.addEvent(title, day, time, duration, clubId, coachId);
    }

    public Set<Event> allEvents() {
        return repository.allEvents();
    }

    public Set<Event> eventsByClub(int clubId) {
        return repository.eventsByClub(clubId);
    }

    public Set<Event> eventsByCoach(int coachId) {
        return repository.eventsByCoach(coachId);
    }
    public Optional<Event> event(int id) {
        return repository.event(id);
    }

    public void removeEvent(int id) {
        repository.removeEvent(id);
    }

    public Optional<Event> modifyEvent(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
        Optional<Event> event = repository.event(id);

        if (event.isEmpty()) return Optional.empty();

        Event modified = new Event(id, title, day, time, duration, clubId, coachId);

        return Optional.of(repository.modifyEvent(id, modified));
    }
}
