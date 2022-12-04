package uj.jwzp.kpnk.GymApp.model;


import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name="coach")
@SequenceGenerator(name = "default_gen", sequenceName = "coach_seq", allocationSize = 1)
public class Coach extends DomainObject {
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column( nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private int yearOfBirth;

    public Coach() {
    }

    public Coach(int id, String firstName, String lastName, int yearOfBirth) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearOfBirth = yearOfBirth;
    }

    public Coach(String firstName, String lastName, int yearOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearOfBirth = yearOfBirth;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Coach) obj;
        return this.getId() == that.getId() &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                this.yearOfBirth == that.yearOfBirth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), firstName, lastName, yearOfBirth);
    }

    @Override
    public String toString() {
        return "Coach[" +
                "id=" + this.getId() + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "yearOfBirth=" + yearOfBirth + ']';
    }
}
