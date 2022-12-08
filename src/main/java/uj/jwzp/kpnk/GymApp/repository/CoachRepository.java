package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Integer> {
}
