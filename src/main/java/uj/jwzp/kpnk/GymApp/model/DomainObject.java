package uj.jwzp.kpnk.GymApp.model;

import javax.persistence.*;

@MappedSuperclass

public class DomainObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    private int id;

    public DomainObject() {
    }

    public DomainObject(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
