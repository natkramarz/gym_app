package uj.jwzp.kpnk.GymApp.service;

import net.minidev.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.*;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateDurationException;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateTimeException;
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
        if (openingHoursMap.get(day) == null) return false;
        LocalTime clubOpeningHour = openingHoursMap.get(day).getFrom();
        LocalTime clubClosingHour = openingHoursMap.get(day).getTo();

        if (startTime.compareTo(clubOpeningHour) >= 0) {
            if (Duration.between(startTime, clubClosingHour).compareTo(duration) >= 0) return true;
            if (clubClosingHour.compareTo(LocalTime.MAX) != 0) return false;
            return isEventBetweenOpeningHours(openingHoursMap, day.plus(1), LocalTime.MIDNIGHT, duration.minus(Duration.between(startTime, LocalTime.MAX)));
        }

        return false;
    }

    private boolean areEventDetailsValid(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, LocalDate eventDate, int peopleLimit) {
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        if (title == null || title.length() == 0) throw new EventTitleFormatException(title);
        if (clubOptional.isEmpty()) throw new ClubNotFoundException(clubId);
        Club club = clubOptional.get();
        if (day != eventDate.getDayOfWeek()) throw new EventTemplateDayOfWeekMismatchException(day, eventDate.getDayOfWeek());
        if (coachRepository.findById(coachId).isEmpty())  throw new CoachNotFoundException(coachId);
        if (peopleLimit < 0) throw new PeopleLimitFormatException(peopleLimit);
        if (eventDate.isBefore(LocalDate.now())) throw new EventPastDateException(eventDate);
        if (duration.compareTo(Duration.ofHours(24)) > 0) throw new EventDurationException();
        if (!isEventBetweenOpeningHours(club.getWhenOpen(), day, time, duration)) throw new EventTimeException(clubId);

        return true;
    }


    public Event createEvent(String title, LocalTime time, Duration duration, int clubId, int coachId, LocalDate eventDate, int peopleLimit) {
        if (!areEventDetailsValid(title, eventDate.getDayOfWeek(), time, duration, clubId, coachId, eventDate, peopleLimit)) return null;
        Event event = new Event(title, eventDate.getDayOfWeek(), time, duration, clubId, coachId, eventDate, peopleLimit);
        return repository.save(event);

    }

    public Event createEventWithTemplate(int templateId, LocalDate eventDate) {
        EventTemplate template = eventTemplateRepository.findById(templateId).orElseThrow(() -> new EventTemplateNotFoundException(templateId));
        if (eventDate.isBefore(LocalDate.now())) throw new EventPastDateException(eventDate);
        if (template.getDay() != eventDate.getDayOfWeek()) throw new EventTemplateDayOfWeekMismatchException(template.getDay(), eventDate.getDayOfWeek());
        Event event = new Event(
                template.getTitle(),
                template.getDay(),
                template.getTime(),
                template.getDuration(),
                template.getClubId(),
                template.getCoachId(),
                eventDate,
                template.getPeopleLimit()
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
        return repository.findByCoachId(coachId);
    }

    public List<Event> eventsByDateAndClubId(LocalDate date, int clubId) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        return repository.findByClubIdAndEventDate(clubId, date);
    }


    public Event event(int id) {
        return repository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    public Event modifyEvent(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, LocalDate eventDate, int peopleLimit) {
        if (!areEventDetailsValid(title, day, time, duration, clubId, coachId, eventDate, peopleLimit)) return null;

        Event modified = new Event(id, title, day, time, duration, clubId, coachId, eventDate, peopleLimit);
        return repository.save(modified);
    }

    public Page<Event> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }

    public void deleteEvent(int id) {
        if (repository.findById(id).isEmpty()) throw new EventNotFoundException(id);
        List<Integer> eventRegistrations = registrationRepository.findByEventId(id).stream()
                .map(Registration::getId)
                .collect(Collectors.toList());
        registrationRepository.deleteAllByIdInBatch(eventRegistrations);
        repository.deleteById(id);
    }

    public Event changeEventDate(int eventId, LocalDate date, LocalTime time) {
        Event event = repository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (date.isBefore(LocalDate.now())) throw new EventPastDateException(date);
        Club club = clubRepository.findById(event.getClubId()).orElseThrow(() -> new ClubNotFoundException(event.getClubId()));
        if (!isEventBetweenOpeningHours(club.getWhenOpen(), date.getDayOfWeek(), time, event.getDuration())) throw new EventTimeException(event.getClubId());
        event.setEventDate(date);
        event.setTime(time);
        return repository.save(event);
    }

}
