package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "default_gen", sequenceName = "event_template_seq", allocationSize = 1)
@Table(name = "event_template")
public class EventTemplate extends DomainObject implements ServiceEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "`day`")
    private DayOfWeek day;

    @Column(columnDefinition = "TIME")
    private LocalTime startTime;

    private Duration duration;

    private int clubId;

    private int coachId;

    private int peopleLimit;

    public EventTemplate() {

    }

    public EventTemplate(String title, DayOfWeek day, LocalTime startTime, Duration duration, int clubId, int coachId, int peopleLimit) {
        this.title = title;
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.clubId = clubId;
        this.coachId = coachId;
        this.peopleLimit = peopleLimit;
    }

    public EventTemplate(int id, String title, DayOfWeek day, LocalTime startTime, Duration duration, int clubId, int coachId, int peopleLimit) {
        super(id);
        this.title = title;
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.clubId = clubId;
        this.coachId = coachId;
        this.peopleLimit = peopleLimit;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime time) {
        this.startTime = time;
    }

    public Duration getDuration() {
        return duration;
    }

    private void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getClubId() {
        return clubId;
    }

    private void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public int getCoachId() {
        return coachId;
    }

    private void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EventTemplate) obj;
        return this.getId() == that.getId() &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.day, that.day) &&
                Objects.equals(this.startTime, that.startTime) &&
                Objects.equals(this.duration, that.duration) &&
                this.clubId == that.clubId &&
                this.coachId == that.coachId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), title, day, startTime, duration, clubId, coachId);
    }

    @Override
    public String toString() {
        return "EventTemplate[" +
                "id=" + this.getId() + ", " +
                "title=" + title + ", " +
                "day=" + day + ", " +
                "time=" + startTime + ", " +
                "duration=" + duration + ", " +
                "clubId=" + clubId + ", " +
                "coachId=" + coachId + ']';
    }

    public int getPeopleLimit() {
        return peopleLimit;
    }

    public void setPeopleLimit(int peopleLimit) {
        this.peopleLimit = peopleLimit;
    }

    public Event toEvent(LocalDate eventDate) {
        return new Event(title, eventDate, duration, startTime, clubId, coachId, peopleLimit);
    }
}
