package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class OpeningHours {

    @Column(name = "value_from", columnDefinition = "TIME", nullable = false)
    private LocalTime from;
    @Column(name = "value_to", columnDefinition = "TIME", nullable = false)
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

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
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

}
