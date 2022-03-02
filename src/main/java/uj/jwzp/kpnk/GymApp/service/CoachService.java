package uj.jwzp.kpnk.GymApp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.controller.CoachController;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class CoachService {

    private final CoachRepository repository;

    @Autowired
    public CoachService(CoachRepository repository) {
        this.repository = repository;
    }

    public Set<Coach> allCoaches() {
        return repository.allCoaches();
    }

    public Optional<Coach> coach(int id) {
        return repository.coach(id);
    }

    public Coach addCoach(String firstName, String lastName, int yearOfBirth) {
        return repository.addCoach(firstName, lastName, yearOfBirth);
    }

    public Optional<Coach> modifyCoach(int id, String firstName, String lastName, int yearOfBirth) {
        Optional<Coach> coach = repository.coach(id);

        if (coach.isEmpty()) return Optional.empty();

        Coach modified = new Coach(id, firstName, lastName, yearOfBirth);

        return Optional.of(repository.modifyCoach(id, modified));
    }

    public void removeCoach(int id) {
        repository.removeCoach(id);
    }
}
