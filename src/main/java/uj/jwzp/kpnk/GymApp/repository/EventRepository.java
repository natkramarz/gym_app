package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.Event;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class EventRepository {

    private final Map<Integer, Event> events = new HashMap<>();
    private final AtomicInteger lastId = new AtomicInteger();

    public Set<Event> allEvents() {
        return Set.copyOf(events.values());
    }

    public Set<Event> eventsByClub(int clubId) {
        return events.values().stream().filter(e -> e.getClubId() == clubId).collect(Collectors.toSet());
    }

    public Set<Event> eventsByCoach(int coachId) {
        return events.values().stream().filter(e -> e.getCoachId() == coachId).collect(Collectors.toSet());
    }

    public Event addEvent(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
        int id = lastId.incrementAndGet();
        Event event = new Event(id, title, day, time, duration, clubId, coachId);
        events.put(id, event);
        return event;
    }

    public Optional<Event> event(int id) {
        return Optional.ofNullable(events.get(id));
    }

    public void removeEvent(int id) {
        events.remove(id);
    }

    public Event modifyEvent(int id, Event event) {
        events.put(id, event);
        return event;
    }
}
