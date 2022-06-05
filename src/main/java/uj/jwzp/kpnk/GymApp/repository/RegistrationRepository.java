package uj.jwzp.kpnk.GymApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.Registration;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    List<Registration> findByEventId(int eventId);
}
