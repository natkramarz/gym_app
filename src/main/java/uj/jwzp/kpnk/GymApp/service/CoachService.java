package uj.jwzp.kpnk.GymApp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
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

    public Coach coach(int id) {
        return repository.coach(id).orElseThrow(() -> new CoachNotFoundException(id));
    }

    public Coach addCoach(String firstName, String lastName, int yearOfBirth) {
        return repository.addCoach(firstName, lastName, yearOfBirth);
    }

    public Coach modifyCoach(int id, String firstName, String lastName, int yearOfBirth) {
        if (repository.coach(id).isEmpty()) throw new CoachNotFoundException(id);

        Coach modified = new Coach(id, firstName, lastName, yearOfBirth);
        return repository.modifyCoach(id, modified);
    }

    public void removeCoach(int id) {
        repository.removeCoach(id);
    }
}
