package dev.protsenko.vaadin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Contacts")
@Getter
@Setter
@EqualsAndHashCode
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String firstName;
    private String secondName;
    private String fatherName;
    private String numberPhone;
    private String email;

}
