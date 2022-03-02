package uj.jwzp.kpnk.GymApp.repository;

import uj.jwzp.kpnk.GymApp.model.Coach;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CoachRepository {

    private final Map<Integer, Coach> coaches = new HashMap<>();
    private final AtomicInteger lastId = new AtomicInteger();

    public Coach addCoach(String firstName, String lastName, int yearOfBirth) {
        int id = lastId.incrementAndGet();
        Coach coach = new Coach(id, firstName, lastName, yearOfBirth);
        coaches.put(id, coach);
        return coach;
    }

    public Coach modifyCoach(int id, Coach coach) {
        coaches.put(id, coach);
        return coach;
    }

    public Set<Coach> allCoaches() {
        return Set.copyOf(coaches.values());
    }

    public Optional<Coach> coach(int id) {
        return Optional.ofNullable(coaches.get(id));
    }

    public void removeCoach(int id) {
        coaches.remove(id);
    }

}
