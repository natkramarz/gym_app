package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "registration")
@SequenceGenerator(name = "default_gen", sequenceName = "registration_seq", allocationSize = 1)
public class Registration extends DomainObject {

    @Column(nullable = false, name = "gym_bro_id")
    private int gymBroId;
    @Column(nullable = false, name="event_id")
    private int eventId;

    public Registration(int id, int eventId, int gymBroId) {
        super(id);
        this.gymBroId = gymBroId;
        this.eventId = eventId;
    }

    public Registration(int eventId, int gymBroId) {
        this.gymBroId = gymBroId;
        this.eventId = eventId;
    }

    public Registration() {
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
        return this.getId() == that.getId() && eventId == that.eventId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), gymBroId, eventId);
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + this.getId() +
                ", gymBroId=" + gymBroId +
                ", eventId=" + eventId +
                '}';
    }

    public int getGymBroId() {
        return gymBroId;
    }

    public void setGymBroId(int gymBroId) {
        this.gymBroId = gymBroId;
    }
}
