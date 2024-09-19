package ru.digitalhabits.homework3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.exception.PersonNotFoundException;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    default Person getById(int id) {
        return this.findById(id)
                        .orElseThrow(
                                () -> new PersonNotFoundException("Person not found"));
    }
}
