package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name="event")
public class Event extends EventTemplate {

    @Column(columnDefinition = "DATE")
    LocalDate eventDate;

    public Event(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, LocalDate eventDate) {
        super(id, title, day, time, duration, clubId, coachId);
        this.eventDate = eventDate;
    }

    public Event(String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId, LocalDate eventDate) {
        super(title, day, time, duration, clubId, coachId);
        this.eventDate = eventDate;
    }

    public Event() {
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Event event = (Event) o;
        return eventDate.equals(event.eventDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eventDate);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventDate=" + eventDate + "title=" + this.getTitle() + ", " +
                "duration=" + this.getDuration() + ", " +
                "clubId=" + this.getClubId() + ", " +
                "coachId=" + this.getCoachId() +
                '}';
    }
}
