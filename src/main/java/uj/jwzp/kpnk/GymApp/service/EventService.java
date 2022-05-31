package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.*;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.repository.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository repository;
    private final ClubRepository clubRepository;
    private final CoachRepository coachRepository;
    private final EventTemplateRepository eventTemplateRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public EventService(EventRepository repository, ClubRepository clubRepository, CoachRepository coachRepository, EventTemplateRepository eventTemplateRepository, RegistrationRepository registrationRepository) {
        this.repository = repository;
        this.clubRepository = clubRepository;
        this.coachRepository = coachRepository;
        this.eventTemplateRepository = eventTemplateRepository;
        this.registrationRepository = registrationRepository;
    }

    public boolean isEventBetweenOpeningHours(Map<DayOfWeek, OpeningHours> openingHoursMap, DayOfWeek day, LocalTime startTime, Duration duration) {
        LocalTime clubOpeningHour = openingHoursMap.get(day).getFrom();
        LocalTime clubClosingHour = openingHoursMap.get(day).getTo();

        if (startTime.compareTo(clubOpeningHour) >= 0) {
            if (Duration.between(startTime, clubClosingHour).compareTo(duration) >= 0) return true;
            if (clubClosingHour.compareTo(LocalTime.MAX) != 0) return false;
            return isEventBetweenOpeningHours(openingHoursMap, day.plus(1), LocalTime.MIDNIGHT, duration.minus(Duration.between(startTime, LocalTime.MAX)));
        }

        return false;
    }

    public Event addEvent(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, LocalDate eventDate) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);
        if (coachRepository.findById(coachId).isEmpty())  throw new CoachNotFoundException(coachId);
        if (duration.compareTo(Duration.ofHours(24)) > 0) throw new EventTemplateDurationException(title);
        if (!isEventBetweenOpeningHours(clubRepository.findById(clubId).get().getWhenOpen(), day, time, duration)) throw new EventTemplateTimeException(title);

        Event event = new Event(title, day, time, duration, clubId, coachId, eventDate);
        return repository.save(event);
    }

    public Event createEventWithTemplate(int templateId, LocalDate eventDate) {
        if (eventTemplateRepository.findById(templateId).isEmpty()) throw new EventTemplateNotFoundException(templateId);
        if (eventDate.isBefore(LocalDate.now())) throw new EventPastDateException(eventDate);
        EventTemplate template = eventTemplateRepository.findById(templateId).get();
        Event event = new Event(
                template.getTitle(),
                template.getDay(),
                template.getTime(),
                template.getDuration(),
                template.getClubId(),
                template.getCoachId(),
                eventDate
        );
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
        if (coachRepository.findById(coachId).isEmpty()) throw new CoachNotFoundException(coachId);

        return repository.findByCoachId(coachId);
    }

    public List<Event> eventsByDateAndClubId(LocalDate date, int clubId) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        return repository.findByClubIdAndEventDate(clubId, date);
    }


    public Event event(int id) {
        return repository.findById(id).orElseThrow(() -> new EventTemplateNotFoundException(id));
    }

    public void removeEvent(int id) {
        if (repository.findById(id).isEmpty()) throw new EventTemplateNotFoundException(id);

        repository.deleteById(id);
    }

    public Event modifyEvent(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, LocalDate eventDate) {
        if (repository.findById(id).isEmpty()) throw new EventTemplateNotFoundException(id);
        if (coachRepository.findById(coachId).isEmpty()) throw new CoachNotFoundException(coachId);
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);
        if (eventDate.isBefore(LocalDate.now())) throw new EventPastDateException(eventDate);
        if (duration.compareTo(Duration.ofHours(24)) > 0) throw new EventTemplateDurationException(title);
        if (!isEventBetweenOpeningHours(clubRepository.findById(clubId).get().getWhenOpen(), day, time, duration)) throw new EventTemplateTimeException(title);

        Event modified = new Event(id, title, day, time, duration, clubId, coachId, eventDate);
        return repository.save(modified);
    }

    public Page<Event> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }

    public void cancelEvent(int id) {
        if (repository.findById(id).isEmpty()) throw new EventNotFoundException(id);
        List<Integer> eventRegistrations = registrationRepository.findByEventId(id).stream()
                .map(registration -> registration.getId())
                .collect(Collectors.toList());
        registrationRepository.deleteAllByIdInBatch(eventRegistrations);
        repository.deleteById(id);
    }

    public Event changeEventDate(int eventId, LocalDate date) {
        Optional<Event> requestedEvent = repository.findById(eventId);
        if (requestedEvent.isEmpty()) throw new EventNotFoundException(eventId);
        if (date.isBefore(LocalDate.now())) throw new EventPastDateException(date);
        Event oldEvent = requestedEvent.get();
        oldEvent.setEventDate(date);
        return repository.save(oldEvent);
    }

}
