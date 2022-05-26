package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;

import java.util.*;

@Repository
public interface EventTemplateRepository extends JpaRepository<EventTemplate, Integer> {
    public List<EventTemplate> findByCoachId(Integer id);
    public List<EventTemplate> findByClubId(Integer id);
}
