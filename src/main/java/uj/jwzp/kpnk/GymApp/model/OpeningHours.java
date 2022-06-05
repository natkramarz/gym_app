package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
@Table(name="opening_hours")
public class OpeningHours {

    @Column(name = "valueFrom", columnDefinition = "TIME")
    private LocalTime from;
    @Column(name = "valueTo", columnDefinition = "TIME")
    private LocalTime to;

    public OpeningHours() {

    }

    public OpeningHours(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalTime getFrom() {
        return from;
    }

    public LocalTime getTo() {
        return to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OpeningHours) obj;
        return Objects.equals(this.from, that.from) &&
                Objects.equals(this.to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "OpeningHours[" +
                "from=" + from + ", " +
                "to=" + to + ']';
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

}
