package ru.digitalhabits.homework3.service;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.digitalhabits.homework3.Validation.DepartmentValidation;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.mapper.DepartmentMapper;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.model.DepartmentShortResponse;
import ru.digitalhabits.homework3.repository.DepartmentRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl
        implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final DepartmentValidation departmentValidation;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentShortResponse> findAll() {
        return Option.of(departmentRepository.findAll())
                .map(departmentMapper::mapToShort)
                .get();
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public DepartmentFullResponse getById(int id) {
        return Option.of(id)
                .map(departmentRepository::getById)
                .map(departmentMapper::map)
                .get();
    }

    @Override
    @Transactional
    public int create(@Nonnull DepartmentRequest request) {
        return Option.of(request)
                .map(departmentMapper::map)
                .map(departmentRepository::save)
                .map(Department::getId)
                .get();
    }

    @Nonnull
    @Override
    @Transactional
    public DepartmentFullResponse update(int id, @Nonnull DepartmentRequest request) {
        return Option.of(id)
                .map(departmentRepository::getById)
                .map(it -> departmentMapper.map(it, request))
                .map(departmentRepository::save)
                .map(departmentMapper::map)
                .get();
    }

    @Override
    @Transactional
    public void delete(int id) {
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void close(int id) {
        Option.of(id)
                .map(departmentRepository::getById)
                .map(this::closeDepartment)
                .map(departmentRepository::save);
    }

    @Override
    public Department addPerson(Person person, int departmentId) {
        return Option.of(person)
                .map(it -> Objects.nonNull(person.getDepartment()) ? person.getDepartment() : departmentRepository.getById(departmentId))
                .peek(departmentValidation::validate)
                .peek(it -> addPersonToDepartment(person, it))
                .map(departmentRepository::save)
                .get();
    }

    @Override
    public void removePerson(Person person) {
        Option.of(person)
                .map(Person::getDepartment)
                .peek(departmentValidation::validate)
                .peek(it -> removePersonToDepartment(person, it))
                .map(departmentRepository::save);
    }

    private void addPersonToDepartment(Person person, Department department) {
        Option.of(department)
                .map(Department::getPersonList)
                .map(it -> it.add(person));
    }

    private void removePersonToDepartment(Person person, Department department) {
        Option.of(department)
                .map(Department::getPersonList)
                .map(it -> it.remove(person));
    }

    private Department closeDepartment(Department department) {
        department.setIsClosed(true);
        department.getPersonList().clear();
        return department;
    }
}
