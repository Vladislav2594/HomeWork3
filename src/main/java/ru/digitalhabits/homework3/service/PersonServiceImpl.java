package ru.digitalhabits.homework3.service;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.mapper.PersonMapper;
import ru.digitalhabits.homework3.model.PersonFullResponse;
import ru.digitalhabits.homework3.model.PersonRequest;
import ru.digitalhabits.homework3.model.PersonShortResponse;
import ru.digitalhabits.homework3.repository.PersonRepository;

import javax.annotation.Nonnull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl
        implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final DepartmentService departmentService;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<PersonShortResponse> findAll() {
        return Option.of(personRepository.findAll())
                .map(personMapper::mapToShort)
                .get();
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public PersonFullResponse getById(int id) {
        return Option.of(id)
                .map(personRepository::getById)
                .map(personMapper::map)
                .get();
    }

    @Override
    @Transactional
    public int create(@Nonnull PersonRequest request) {
        return Option.of(request)
                .map(personMapper::map)
                .map(personRepository::save)
                .map(Person::getId)
                .get();
    }

    @Nonnull
    @Override
    @Transactional
    public PersonFullResponse update(int id, @Nonnull PersonRequest request) {
        return Option.of(id)
                .map(personRepository::getById)
                .map(it -> personMapper.map(it, request))
                .map(personRepository::save)
                .map(personMapper::map)
                .get();
    }

    @Override
    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void addPersonToDepartment(int departmentId, int personId) {
        Option.of(personId)
                .map(personRepository::getById)
                .peek(it -> it.setDepartment(departmentService.addPerson(it, departmentId)))
                .map(personRepository::save);
    }

    @Override
    @Transactional
    public void removePersonFromDepartment(int departmentId, int personId) {
        Option.of(personId)
                .map(personRepository::getById)
                .peek(departmentService::removePerson)
                .peek(this::removeDepartment)
                .map(personRepository::save);
    }

    private void removeDepartment(Person person) {
        Option.of(person)
                .map(it -> it.setDepartment(null));
    }
}
