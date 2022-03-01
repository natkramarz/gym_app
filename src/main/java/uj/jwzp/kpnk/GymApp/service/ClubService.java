package uj.jwzp.kpnk.GymApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ClubService {

    private ClubRepository repository;

    @Autowired
    public ClubService(ClubRepository repository) {
        this.repository = repository;
    }

    public Set<Club> allClubs() {
        return repository.allClubs();
    }

    public Optional<Club> club(int id) {
        return repository.club(id);
    }

    public Club addClub(String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        return repository.addClub(name, address, whenOpen);
    }

    public Optional<Club> modifyClub(int id, String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        Optional<Club> club = repository.club(id);
        if (club.isEmpty()) return Optional.empty();

        Club modified = new Club(id, name, address, whenOpen);
        return Optional.of(repository.modifyClub(id, modified));
    }

    public void removeClub(int id) {
        repository.removeClub(id);
    }
}
