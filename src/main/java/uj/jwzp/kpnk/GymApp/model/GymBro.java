package uj.jwzp.kpnk.GymApp.model;


import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "default_gen", sequenceName = "event_template_seq", allocationSize = 1)
@Table(name = "gym_bro")
public class GymBro extends DomainObject implements ServiceEntity {
}
