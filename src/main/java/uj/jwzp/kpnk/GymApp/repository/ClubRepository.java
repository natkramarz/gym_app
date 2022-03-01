package uj.jwzp.kpnk.GymApp.repository;

import org.springframework.stereotype.Repository;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ClubRepository {

    private final Map<Integer, Club> clubs = new HashMap<>();
    private final AtomicInteger lastId = new AtomicInteger();

    public Club addClub(String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        var id = lastId.incrementAndGet();
        var club = new Club(id, name, address, whenOpen);
        clubs.put(id, club);
        return club;
    }

    public Club modifyClub(int id, Club club) {
        clubs.put(id, club);
        return club;
    }

    public Set<Club> allClubs() {
        return Set.copyOf(clubs.values());
    }

    public Optional<Club> club(int id) {
        return Optional.ofNullable(clubs.get(id));
    }

    public void removeClub(int id) {
        clubs.remove(id);
    }
}
