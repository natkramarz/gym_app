package uj.jwzp.kpnk.GymApp.model;

import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Map;
import java.util.Objects;

@Entity
public class Club extends RepresentationModel<Club> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private int id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    private Map<DayOfWeek, OpeningHours> whenOpen;

    public Club() {
    }

    public Club(int id, String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.whenOpen = whenOpen;
    }

    public Club(String name, String address, Map<DayOfWeek, OpeningHours> whenOpen) {
        this.name = name;
        this.address = address;
        this.whenOpen = whenOpen;
    }

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Map<DayOfWeek, OpeningHours> getWhenOpen() {
        return whenOpen;
    }

    @Override
    public String toString() {
        return "Club[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "address=" + address + ", " +
                "whenOpen=" + whenOpen + ']';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWhenOpen(Map<DayOfWeek, OpeningHours> whenOpen) {
        this.whenOpen = whenOpen;
    }
}
