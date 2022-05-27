package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.Event;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    public List<Event> findByCoachId(Integer id);
    public List<Event> findByClubId(Integer id);
    public List<Event> findByClubIdAndEventDate(Integer id, LocalDate eventDate);
}
