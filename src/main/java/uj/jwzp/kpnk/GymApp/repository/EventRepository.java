package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.Event;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Set<Event> findByCoachId(Integer id);

    Set<Event> findByClubId(Integer id);

    Set<Event> findByClubIdAndEventDate(Integer id, LocalDate eventDate);

    Set<Event> findByCoachIdAndEventDate(Integer coachId, LocalDate eventDate);

    Set<Event> removeByEventDateBefore(LocalDate eventDate);
}
