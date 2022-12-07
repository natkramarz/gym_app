package uj.jwzp.kpnk.GymApp.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Objects;

@Relation(itemRelation = "club", collectionRelation = "clubs")
public class ClubRepresentation extends RepresentationModel<ClubRepresentation> {

    private int id;
    private String name;
    private String address;
    private Map<DayOfWeek, OpeningHours> whenOpen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
        return id == club.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<DayOfWeek, OpeningHours> getWhenOpen() {
        return whenOpen;
    }

    public void setWhenOpen(Map<DayOfWeek, OpeningHours> whenOpen) {
        this.whenOpen = whenOpen;
    }

    @Override
    public String toString() {
        return "Club[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "address=" + address + ", " +
                "whenOpen=" + whenOpen + ']';
    }
}
