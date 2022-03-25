package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.Coach;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Integer> {

    final AtomicInteger lastId = new AtomicInteger();

    public default Coach addCoach(String firstName, String lastName, int yearOfBirth) {
        int id = lastId.incrementAndGet();
        Coach coach = new Coach(id, firstName, lastName, yearOfBirth);
        return saveAndFlush(coach);
    }

    /*

    // findAll
    public Set<Coach> findAll() {
        return Set.copyOf(coaches.values());
    }

    //getByID
    public Optional<Coach> coach(int id) {
        return Optional.ofNullable(coaches.get(id));
    }

    // deleteAllByIdInBatch
    public void removeCoach(int id) {
        coaches.remove(id);
    }
    */
}
