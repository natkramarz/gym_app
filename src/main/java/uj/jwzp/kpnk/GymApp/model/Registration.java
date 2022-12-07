package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "registration")
@SequenceGenerator(name = "default_gen", sequenceName = "registration_seq", allocationSize = 1)
public class Registration extends DomainObject implements ServiceEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private int eventId;

    public Registration(int id, String name, String surname, int eventId) {
        super(id);
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
        return this.getId() == that.getId() && eventId == that.eventId && name.equals(that.name) && surname.equals(that.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), name, surname, eventId);
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", eventId=" + eventId +
                '}';
    }
}
