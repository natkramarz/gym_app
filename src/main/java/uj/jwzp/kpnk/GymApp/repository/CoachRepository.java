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
}
