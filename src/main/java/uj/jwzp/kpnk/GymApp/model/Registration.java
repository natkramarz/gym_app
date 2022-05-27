package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private int id;

    private String name;
    private String surname;
    private int eventId;

    public Registration(int id, String name, String surname, int eventId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.eventId = eventId;
    }

    public Registration(String name, String surname, int eventId) {
        this.name = name;
        this.surname = surname;
        this.eventId = eventId;
    }

    public Registration() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return id == that.id && eventId == that.eventId && name.equals(that.name) && surname.equals(that.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, eventId);
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", eventId=" + eventId +
                '}';
    }
}
