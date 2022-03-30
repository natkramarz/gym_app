package uj.jwzp.kpnk.GymApp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.AssignedEventsException;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class CoachService {

    private final CoachRepository repository;
    private final EventService eventService;

    @Autowired
    public CoachService(CoachRepository repository, EventService eventService) {
        this.repository = repository;
        this.eventService = eventService;
    }

    public Set<Coach> allCoaches() {
        return new HashSet<>(repository.findAll());
    }

    public Coach coach(int id) {
        return repository.findById(id).orElseThrow(() -> new CoachNotFoundException(id));
    }

    public Coach addCoach(String firstName, String lastName, int yearOfBirth) {
        Coach coach = new Coach(firstName, lastName, yearOfBirth);
        return repository.save(coach);
    }

    public Coach modifyCoach(int id, String firstName, String lastName, int yearOfBirth) {
        if (repository.findById(id).isEmpty()) throw new CoachNotFoundException(id);
        Coach modified = new Coach(id, firstName, lastName, yearOfBirth);
        return repository.save(modified);
    }

    public void removeCoach(int id) {
        if (repository.findById(id).isEmpty()) throw new CoachNotFoundException(id);
        if (!eventService.eventsByCoach(id).isEmpty()) throw new AssignedEventsException(id);
        repository.deleteById(id);
    }
}
