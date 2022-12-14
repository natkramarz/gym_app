package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uj.jwzp.kpnk.GymApp.controller.request.CreateRequest;
import uj.jwzp.kpnk.GymApp.controller.request.EventTemplateCreateRequest;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventTitleFormatException;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateDurationException;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event_template.EventTemplateTimeException;
import uj.jwzp.kpnk.GymApp.exception.event_template.PeopleLimitFormatException;
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
public class EventTemplateService implements ServiceLayer<EventTemplate> {

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
            if (clubClosingHour.compareTo(LocalTime.MAX) != 0) return false;
            return isEventTemplateBetweenOpeningHours(openingHoursMap, day.plus(1), LocalTime.MIDNIGHT, duration.minus(Duration.between(startTime, LocalTime.MAX)));
        }

        return false;
    }

    private boolean areEventTemplateDetailsValid(EventTemplate eventTemplate) {
        if (clubRepository.findById(eventTemplate.getClubId()).isEmpty())
            throw new ClubNotFoundException(eventTemplate.getClubId());
        if (eventTemplate.getTitle() == null || eventTemplate.getTitle().length() == 0)
            throw new EventTitleFormatException(eventTemplate.getTitle());
        if (coachRepository.findById(eventTemplate.getCoachId()).isEmpty())
            throw new CoachNotFoundException(eventTemplate.getCoachId());
        if (eventTemplate.getDuration().compareTo(Duration.ofHours(24)) > 0)
            throw new EventTemplateDurationException(eventTemplate.getTitle());
        if (eventTemplate.getPeopleLimit() < 0) throw new PeopleLimitFormatException(eventTemplate.getPeopleLimit());
        if (!isEventTemplateBetweenOpeningHours(clubRepository.findById(eventTemplate.getClubId()).get().getWhenOpen(), eventTemplate.getDay(), eventTemplate.getStartTime(), eventTemplate.getDuration()))
            throw new EventTemplateTimeException(eventTemplate.getTitle());

        return true;
    }

    public EventTemplate createEventTemplate(EventTemplateCreateRequest request) {
        if (!areEventTemplateDetailsValid(request.asObject()))
            return null;

        EventTemplate eventTemplate = request.asObject();
        return repository.save(eventTemplate);
    }

    public List<EventTemplate> eventTemplatesByClub(int clubId) {
        if (clubRepository.findById(clubId).isEmpty()) throw new ClubNotFoundException(clubId);

        return repository.findByClubId(clubId);
    }

    public List<EventTemplate> eventTemplatesByCoach(int coachId) {
        if (coachRepository.findById(coachId).isEmpty()) throw new CoachNotFoundException(coachId);

        return repository.findByCoachId(coachId);
    }


    public void deleteEventTemplatesByCoach(int coachId) {
        repository.deleteAllByCoachId(coachId);
    }

    public void deleteEventTemplatesByClub(List<Integer> eventIds) {
        repository.deleteAllByIdInBatch(eventIds);
    }

    public Page<EventTemplate> findPaginated(@RequestParam("page") int pageNumber, @RequestParam("size") int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(paging);
    }

    @Override
    public List<EventTemplate> getAll() {
        return repository.findAll();
    }

    @Override
    public EventTemplate get(int id) {
        return repository.findById(id).orElseThrow(() -> new EventTemplateNotFoundException(id));
    }

    @Override
    public EventTemplate add(CreateRequest<EventTemplate> createRequest) {
        if (!areEventTemplateDetailsValid(createRequest.asObject()))
            return null;

        EventTemplate eventTemplate = createRequest.asObject();
        return repository.save(eventTemplate);
    }

    @Override
    public EventTemplate modify(int id, CreateRequest<EventTemplate> createRequest) {
        if (repository.findById(id).isEmpty()) throw new EventTemplateNotFoundException(id);
        if (!areEventTemplateDetailsValid(createRequest.asObject()))
            return null;

        EventTemplate modified = createRequest.asObject();
        return repository.save(modified);
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isEmpty()) throw new EventTemplateNotFoundException(id);

        repository.deleteById(id);
    }
}
