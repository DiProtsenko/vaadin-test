package dev.protsenko.vaadin.repositories;

import dev.protsenko.vaadin.entities.Contact;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactRepository extends CrudRepository<Contact, Integer> {
    List<Contact> findAll();
}
