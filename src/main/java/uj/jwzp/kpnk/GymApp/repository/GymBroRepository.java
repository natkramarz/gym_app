package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.jwzp.kpnk.GymApp.model.GymBro;

import java.util.List;

public interface GymBroRepository extends JpaRepository<GymBro, Integer> {
    List<GymBro> findAllById(Iterable<Integer> integers);
}
