package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(
        name = "event"
)
public class Event {

    @Id
    @SequenceGenerator(
            name = "event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )

    @Column(
            name = "id",
            updatable = false
    )
    private int id;

    @Column(
            name = "title",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String title;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Column(name = "time", columnDefinition = "TIME")
    private LocalTime time;


    @Column(name = "duration")
    private Duration duration;

    @Column(name = "club_id")
    private int clubId;

    @Column(name = "coach_id")
    private int coachId;

    public Event() {

    }

    public Event(int id, String title, DayOfWeek day, LocalTime time, Duration duration, int clubId, int coachId) {
        this.id = id;
        this.title = title;
        this.day = day;
        this.time = time;
        this.duration = duration;
        this.clubId = clubId;
        this.coachId = coachId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getTime() {
        return time;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getClubId() {
        return clubId;
    }

    public int getCoachId() {
        return coachId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Event) obj;
        return this.id == that.id &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.day, that.day) &&
                Objects.equals(this.time, that.time) &&
                Objects.equals(this.duration, that.duration) &&
                this.clubId == that.clubId &&
                this.coachId == that.coachId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, day, time, duration, clubId, coachId);
    }

    @Override
    public String toString() {
        return "Event[" +
                "id=" + id + ", " +
                "title=" + title + ", " +
                "day=" + day + ", " +
                "time=" + time + ", " +
                "duration=" + duration + ", " +
                "clubId=" + clubId + ", " +
                "coachId=" + coachId + ']';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }
}
