package uj.jwzp.kpnk.GymApp.model;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Objects;

public record Club(int id, String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
        return id == club.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
