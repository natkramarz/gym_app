package uj.jwzp.kpnk.GymApp.model;


import org.hibernate.annotations.SQLDelete;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "default_gen", sequenceName = "gym_bro_seq", allocationSize = 1)
@SQLDelete(sql = "UPDATE gym_bro SET deleted = true WHERE id=?")
@Table(name = "gym_bro")
public class GymBro extends DomainObject {

    @Column(nullable = false, length = 50)
    String firstName;
    @Column(nullable = false, length = 50)
    String lastName;
    @Column(columnDefinition = "DATE")
    LocalDate accountCreatedAt;

    private boolean deleted = Boolean.FALSE;

    public GymBro(int id, String firstName, String lastName, LocalDate accountCreatedAt, boolean deleted) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountCreatedAt = accountCreatedAt;
        this.deleted = deleted;
    }

    public GymBro(String firstName, String lastName, LocalDate accountCreatedAt) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountCreatedAt = accountCreatedAt;
    }

    public GymBro() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getAccountCreatedAt() {
        return accountCreatedAt;
    }

    public void setAccountCreatedAt(LocalDate accountCreatedAt) {
        this.accountCreatedAt = accountCreatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GymBro gymBro = (GymBro) o;
        return firstName.equals(gymBro.firstName) && lastName.equals(gymBro.lastName) && accountCreatedAt.equals(gymBro.accountCreatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, accountCreatedAt);
    }

    @Override
    public String toString() {
        return "GymBro{" +
                super.getId() +
                " firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountCreatedAt=" + accountCreatedAt +
                '}';
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
