package uj.jwzp.kpnk.GymApp.model;


import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name="coach")
public class Coach implements ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private int id;

    @Column(nullable = false, length = 50)
    private String firstName;
    @Column( nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private int yearOfBirth;

    public Coach() {
    }

    public Coach(int id, String firstName, String lastName, int yearOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearOfBirth = yearOfBirth;
    }

    public Coach(String firstName, String lastName, int yearOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearOfBirth = yearOfBirth;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getId() {
        return id;
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
        return this.id == that.id &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                this.yearOfBirth == that.yearOfBirth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, yearOfBirth);
    }

    @Override
    public String toString() {
        return "Coach[" +
                "id=" + id + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "yearOfBirth=" + yearOfBirth + ']';
    }
}
