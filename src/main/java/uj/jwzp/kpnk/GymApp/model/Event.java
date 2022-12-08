package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "event")
public class Event extends EventTemplate {

    @Column(columnDefinition = "DATE")
    LocalDate eventDate;

    public Event(int id, String title, DayOfWeek day, LocalTime startTime, Duration duration, int clubId, int coachId, LocalDate eventDate, int peopleLimit) {
        super(id, title, eventDate.getDayOfWeek(), startTime, duration, clubId, coachId, peopleLimit);
        this.eventDate = eventDate;
    }

    public Event(String title, LocalDate eventDate, Duration duration, LocalTime startTime, int clubId, int coachId, int peopleLimit) {
        super(title, eventDate.getDayOfWeek(), startTime, duration, clubId, coachId, peopleLimit);
        this.eventDate = eventDate;
    }

    public Event(int id, String title, LocalDate eventDate, Duration duration, LocalTime startTime, int clubId, int coachId, int peopleLimit) {
        super(id, title, eventDate.getDayOfWeek(), startTime, duration, clubId, coachId, peopleLimit);
        this.eventDate = eventDate;
    }

    public Event(EventTemplate eventTemplate, LocalDate eventDate) {
        super(
                eventTemplate.getTitle(),
                eventTemplate.getDay(),
                eventTemplate.getStartTime(),
                eventTemplate.getDuration(),
                eventTemplate.getClubId(),
                eventTemplate.getCoachId(),
                eventTemplate.getPeopleLimit()
        );
        this.eventDate = eventDate;
    }


    public Event() {
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
        this.setDay(eventDate.getDayOfWeek());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event that = (Event) o;
        if (!(Objects.equals(this.getTitle(), that.getTitle()) &&
                Objects.equals(this.getStartTime(), that.getStartTime()) &&
                Objects.equals(this.getDuration(), that.getDuration()) &&
                this.getClubId() == that.getClubId() &&
                this.getCoachId() == that.getCoachId())) return false;
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
                "eventDate=" + eventDate +
                "title=" + this.getTitle() + ", " +
                "eventDay=" + this.getDay() + ", " +
                "duration=" + this.getDuration() + ", " +
                "startTime=" + this.getStartTime() + ", " +
                "clubId=" + this.getClubId() + ", " +
                "coachId=" + this.getCoachId() +
                '}';
    }
}
