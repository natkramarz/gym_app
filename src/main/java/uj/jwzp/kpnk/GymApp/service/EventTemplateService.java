package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventTitleFormatException;
import uj.jwzp.kpnk.GymApp.exception.event_template.PeopleLimitFormatException;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateDurationException;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateTimeException;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;
import uj.jwzp.kpnk.GymApp.repository.EventTemplateRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class EventTemplateService {

    private final EventTemplateRepository repository;
    private final ClubRepository clubRepository;
    private final CoachRepository coachRepository;

    @Autowired
    public EventTemplateService(EventTemplateRepository repository, ClubRepository clubRepository, CoachRepository coachRepository) {
        this.repository = repository;
        this.clubRepository = clubRepository;
        this.coachRepository = coachRepository;
    }

    public boolean isEventTemplateBetweenOpeningHours(Map<DayOfWeek, OpeningHours> openingHoursMap, DayOfWeek day, LocalTime startTime, Duration duration) {
        LocalTime clubOpeningHour = openingHoursMap.get(day).getFrom();
        LocalTime clubClosingHour = openingHoursMap.get(day).getTo();

        if (startTime.compareTo(clubOpeningHour) >= 0) {
            if (Duration.between(startTime, clubClosingHour).compareTo(duration) >= 0) return true;
            if (clubClosingHour.compareTo(LocalTime.MAX) != 0) return false;return isEventTemplateBetweenOpeningHours(openingHoursMap, day.plus(1), LocalTime.MIDNIGHT, duration.minus(Duration.between(startTime, LocalTime.MAX)));
        }

        return false;
    }

    private boolean areEventTemplateDetailsValid(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, int peopleLimit) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);
        if (title == null || title.length() == 0) throw new EventTitleFormatException(title);
        if (coachRepository.findById(coachId).isEmpty())  throw new CoachNotFoundException(coachId);
        if (duration.compareTo(Duration.ofHours(24)) > 0) throw new EventTemplateDurationException(title);
        if (peopleLimit < 0) throw new PeopleLimitFormatException(peopleLimit);
        if (!isEventTemplateBetweenOpeningHours(clubRepository.findById(clubId).get().getWhenOpen(), day, time, duration)) throw new EventTemplateTimeException(title);

        return true;
    }

    public EventTemplate createEventTemplate(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, int peopleLimit) {
        if (!areEventTemplateDetailsValid(title, day, time, duration, clubId, coachId, peopleLimit)) return null;

        EventTemplate eventTemplate = new EventTemplate(title, day, time, duration, clubId, coachId, peopleLimit);
        return repository.save(eventTemplate);
    }

    public List<EventTemplate> allEventTemplates() {
        return repository.findAll();
    }

    public List<EventTemplate> eventTemplatesByClub(int clubId) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        return repository.findByClubId(clubId);
    }

    public List<EventTemplate> eventTemplatesByCoach(int coachId) {
        if (coachRepository.findById(coachId).isEmpty()) throw new CoachNotFoundException(coachId);

        return repository.findByCoachId(coachId);
    }


    public EventTemplate eventTemplate(int id) {
        return repository.findById(id).orElseThrow(() -> new EventTemplateNotFoundException(id));
    }

    public void deleteEventTemplate(int id) {
        if (repository.findById(id).isEmpty()) throw new EventTemplateNotFoundException(id);

        repository.deleteById(id);
    }

    public void deleteEventTemplatesByCoach(int coachId) {
        repository.deleteAllByCoachId(coachId);
    }

    public void deleteEventTemplatesByClub(List<Integer> eventIds) {
        repository.deleteAllByIdInBatch(eventIds);
    }

    public EventTemplate modifyEventTemplate(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, int peopleLimit) {
        if (repository.findById(id).isEmpty()) throw new EventTemplateNotFoundException(id);
        if (!areEventTemplateDetailsValid(title, day, time, duration, clubId, coachId, peopleLimit)) return null;

        EventTemplate modified = new EventTemplate(id, title, day, time, duration, clubId, coachId, peopleLimit);
        return repository.save(modified);
    }

    public Page<EventTemplate> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }
}
