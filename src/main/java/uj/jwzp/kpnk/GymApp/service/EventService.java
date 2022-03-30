package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventDurationException;
import uj.jwzp.kpnk.GymApp.exception.event.EventNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventTimeException;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public boolean isEventTimeBetweenClubOpeningHours(Map<DayOfWeek, OpeningHours> openingHoursMap, DayOfWeek day, LocalTime time, Duration duration) {
        LocalTime clubFrom = openingHoursMap.get(day).getFrom();
        LocalTime clubTo = openingHoursMap.get(day).getTo();
        if (time.compareTo(clubFrom) >= 0) {
            if (Duration.between(time, clubTo).compareTo(duration) >= 0) return true;
            if (clubTo.compareTo(LocalTime.MAX) != 0) return false;
            return isEventTimeBetweenClubOpeningHours(openingHoursMap, day.plus(1), LocalTime.MIDNIGHT, duration.minus(Duration.between(time, LocalTime.MAX)));
        }
        return false;
    }

    public Event addEvent(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);
        try {
            coachRepository.getById(coachId);
        } catch (EntityNotFoundException e){
            throw new CoachNotFoundException(coachId);
        }
        if (duration.compareTo(Duration.ofHours(24)) > 0) throw new EventDurationException(title);
        if (!isEventTimeBetweenClubOpeningHours(clubRepository.findById(clubId).get().getWhenOpen(), day, time, duration)) throw new EventTimeException(title);
        Event event = new Event(title, day, time, duration, clubId, coachId);
        return repository.save(event);
    }

    public List<Event> allEvents() {
        return repository.findAll();
    }

    public List<Event> eventsByClub(int clubId) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        return repository.findByClubId(clubId);
    }

    public List<Event> eventsByCoach(int coachId) {
        try {
            coachRepository.getById(coachId);
        } catch (EntityNotFoundException e){
            throw new CoachNotFoundException(coachId);
        }

        return repository.findByCoachId(coachId);
    }


    public Event event(int id) {
        return repository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    public void removeEvent(int id) {
        if (repository.findById(id).isEmpty()) throw new EventNotFoundException(id);

        repository.deleteById(id);
    }

    public Event modifyEvent(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
        if (repository.findById(id).isEmpty()) throw new EventNotFoundException(id);
        try {
            coachRepository.getById(coachId);
        } catch (EntityNotFoundException e){
            throw new CoachNotFoundException(coachId);
        }
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);
        if (duration.compareTo(Duration.ofHours(24)) > 0) throw new EventDurationException(title);
        if (!isEventTimeBetweenClubOpeningHours(clubRepository.findById(clubId).get().getWhenOpen(), day, time, duration)) throw new EventTimeException(title);

        Event modified = new Event(id, title, day, time, duration, clubId, coachId);
        return repository.save(modified);
    }

    public Page<Event> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }
}
