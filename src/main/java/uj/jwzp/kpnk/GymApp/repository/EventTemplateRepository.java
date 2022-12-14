package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface EventTemplateRepository extends JpaRepository<EventTemplate, Integer> {
    List<EventTemplate> findByCoachId(Integer id);

    List<EventTemplate> findByClubId(Integer id);

    List<EventTemplate> deleteAllByCoachId(Integer id);

    List<EventTemplate> findByDay(DayOfWeek dayOfWeek);
}
