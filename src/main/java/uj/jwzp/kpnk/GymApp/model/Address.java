package uj.jwzp.kpnk.GymApp.model;


import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

    String street;
    int localNumber;
    String city;
    String zipCode;

    public Address(String street, int localNumber, String city, String zipCode) {
        this.street = street;
        this.localNumber = localNumber;
        this.city = city;
        this.zipCode = zipCode;
    }

    public Address() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return localNumber == address.localNumber && street.equals(address.street) && city.equals(address.city) && Objects.equals(zipCode, address.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, localNumber, city, zipCode);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", localNumber=" + localNumber +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(int localNumber) {
        this.localNumber = localNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
