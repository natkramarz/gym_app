package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachAlreadyBookedException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.*;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event_template.PeopleLimitFormatException;
import uj.jwzp.kpnk.GymApp.model.*;
import uj.jwzp.kpnk.GymApp.repository.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService implements ServiceLayer<Event> {

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
        if (openingHoursMap.get(day) == null) return false;
        var clubOpeningHour = openingHoursMap.get(day).getFrom();
        var clubClosingHour = openingHoursMap.get(day).getTo();

        if (startTime.compareTo(clubOpeningHour) >= 0) {
            if (Duration.between(startTime, clubClosingHour).compareTo(duration) >= 0) return true;
            if (clubClosingHour.compareTo(LocalTime.MAX) != 0) return false;
            return isEventBetweenOpeningHours(openingHoursMap, day.plus(1), LocalTime.MIDNIGHT, duration.minus(Duration.between(startTime, LocalTime.MAX)));
        }

        return false;
    }

    private boolean isCoachBooked(int coachId, LocalDate date, LocalTime startTime, Duration duration) {
        var endTime = startTime.plus(duration);
        return repository.findByCoachIdAndEventDate(coachId, date).stream()
                .anyMatch(event -> {
                    var start = event.getStartTime();
                    var end = event.getStartTime().plus(event.getDuration());
                    return (start.isAfter(startTime) && start.isBefore(endTime)) ||
                            (end.isAfter(startTime) && end.isBefore(endTime));
                });
    }

    private boolean areEventDetailsValid(Event event) {
        var clubOptional = clubRepository.findById(event.getClubId());
        if (event.getTitle() == null || event.getTitle().length() == 0)
            throw new EventTitleFormatException(event.getTitle());
        if (clubOptional.isEmpty()) throw new ClubNotFoundException(event.getClubId());
        var club = clubOptional.get();
        if (event.getDay() != event.getEventDate().getDayOfWeek())
            throw new EventTemplateDayOfWeekMismatchException(event.getDay(), event.getEventDate().getDayOfWeek());
        if (coachRepository.findById(event.getCoachId()).isEmpty())
            throw new CoachNotFoundException(event.getCoachId());
        if (event.getPeopleLimit() < 0) throw new PeopleLimitFormatException(event.getPeopleLimit());
        if (event.getEventDate().isBefore(LocalDate.now())) throw new EventPastDateException(event.getEventDate());
        if (event.getDuration().compareTo(Duration.ofHours(24)) > 0) throw new EventDurationException();
        if (isCoachBooked(event.getCoachId(), event.getEventDate(), event.getStartTime(), event.getDuration()))
            throw new CoachAlreadyBookedException(event.getCoachId());
        if (!isEventBetweenOpeningHours(club.getWhenOpen(), event.getDay(), event.getStartTime(), event.getDuration()))
            throw new EventTimeException(event.getClubId());

        return true;
    }

    @Override
    public List<Event> getAll() {
        return repository.findAll();
    }

    @Override
    public Event get(int id) {
        return repository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    @Override
    public Event add(CreateRequest<Event> request) {
        if (!areEventDetailsValid(request.asObject()))
            return null;

        var event = request.asObject();
        return repository.save(event);

    }

    public Event createEventWithTemplate(int templateId, LocalDate eventDate) {
        var template = eventTemplateRepository.findById(templateId).orElseThrow(() -> new EventTemplateNotFoundException(templateId));
        if (eventDate.isBefore(LocalDate.now())) throw new EventPastDateException(eventDate);
        if (template.getDay() != eventDate.getDayOfWeek())
            throw new EventTemplateDayOfWeekMismatchException(template.getDay(), eventDate.getDayOfWeek());
        if (isCoachBooked(template.getCoachId(), eventDate, template.getStartTime(), template.getDuration()))
            throw new CoachAlreadyBookedException(template.getCoachId());
        var event = template.toEvent(eventDate);
        return repository.save(event);
    }


    public Set<Event> eventsByClub(int clubId) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        return repository.findByClubId(clubId);
    }

    public Set<Event> eventsByCoach(int coachId) {
        return repository.findByCoachId(coachId);
    }

    public Set<Event> eventsByDateAndClubId(LocalDate date, int clubId) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        return repository.findByClubIdAndEventDate(clubId, date);
    }


    @Override
    public Event modify(int id, CreateRequest<Event> request) {
        if (!areEventDetailsValid(request.asObject()))
            return null;

        var modified = request.asObject(id);
        return repository.save(modified);
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isEmpty()) throw new EventNotFoundException(id);
        List<Integer> eventRegistrations = registrationRepository.findByEventId(id).stream()
                .map(Registration::getId)
                .collect(Collectors.toList());
        registrationRepository.deleteAllByIdInBatch(eventRegistrations);
        repository.deleteById(id);
    }

    public Page<Event> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }


    public Event changeEventDate(int eventId, LocalDate date, LocalTime time) {
        var event = repository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (date.isBefore(LocalDate.now())) throw new EventPastDateException(date);
        Club club = clubRepository.findById(event.getClubId()).orElseThrow(() -> new ClubNotFoundException(event.getClubId()));
        if (!isEventBetweenOpeningHours(club.getWhenOpen(), date.getDayOfWeek(), time, event.getDuration()))
            throw new EventTimeException(event.getClubId());
        event.setEventDate(date);
        event.setStartTime(time);
        return repository.save(event);
    }

    public void archiveEvents(int numOfDays) {
        repository.removeByEventDateBefore(LocalDate.now().minusDays(numOfDays));
    }

    public void addEvents(int numOfDays) {
        var eventTemplates = eventTemplateRepository.findByDay(DayOfWeek.from(LocalDate.now()));
        var createdEvents = eventTemplates.stream()
                .map(eventTemplate -> new Event(eventTemplate, LocalDate.now().plusDays(numOfDays)))
                .filter(this::areEventDetailsValid)
                .toList();
        repository.saveAll(createdEvents);
    }


}
