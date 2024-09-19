package ru.digitalhabits.homework3.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.exception.PersonNotFoundException;
import ru.digitalhabits.homework3.mapper.DepartmentMapper;
import ru.digitalhabits.homework3.mapper.PersonMapper;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.PersonFullResponse;
import ru.digitalhabits.homework3.model.PersonRequest;
import ru.digitalhabits.homework3.model.PersonShortResponse;
import ru.digitalhabits.homework3.repository.PersonRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private DepartmentMapper departmentMapper;
    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        // Arrange
        Person person = new Person();
        when(personRepository.findAll()).thenReturn(List.of(person));
        when(personMapper.mapToShort(anyList())).thenReturn(Collections.singletonList(new PersonShortResponse()));

        // Act
        List<PersonShortResponse> result = personService.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(personRepository, times(1)).findAll();
        verify(personMapper, times(1)).mapToShort(anyList());
    }

    @Test
    void findById() {
        // Arrange
        Person person = new Person();
        when(personRepository.findById(anyInt())).thenReturn(Optional.of(person));
        when(personMapper.map(any(Person.class))).thenReturn(new PersonFullResponse());

        // Act
        PersonFullResponse result = personService.getById(1);

        // Assert
        assertNotNull(result);
        verify(personRepository, times(1)).findById(anyInt());
        verify(personMapper, times(1)).map(any(Person.class));
    }

    @Test
    void findById_NotFound() {
        // Arrange
        when(personRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonNotFoundException.class, () -> personService.getById(1));
        verify(personRepository, times(1)).findById(anyInt());
    }

    @Test
    void create() {
        // Arrange
        Person person = new Person();
        person.setId(1);
        PersonRequest request = new PersonRequest();
        when(personMapper.map(any(PersonRequest.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // Act
        int result = personService.create(request);

        // Assert
        assertEquals(1, result);
        verify(personMapper, times(1)).map(any(PersonRequest.class));
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void update() {
        // Arrange
        Person person = new Person();
        person.setId(1);
        person.setAge(32);
        PersonFullResponse personFullResponse = new PersonFullResponse();
        personFullResponse.setId(1);
        personFullResponse.setAge(32);
        PersonRequest request = new PersonRequest();
        request.setAge(person.getAge());
        when(personRepository.findById(anyInt())).thenReturn(Optional.of(person));
        when(personMapper.map(any(Person.class))).thenReturn(personFullResponse);
        when(personMapper.map(any(Person.class), any(PersonRequest.class))).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(personMapper.map(any(Person.class))).thenReturn(new PersonFullResponse());

        // Act
        PersonFullResponse result = personService.update(1, request);

        // Assert
        assertNotNull(result);
        verify(personRepository, times(1)).findById(anyInt());
        verify(personMapper, times(1)).map(any(Person.class), any(PersonRequest.class));
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void delete() {
        // Act
        personService.delete(1);

        // Assert
        verify(personRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void addPersonToDepartment() {
        // Arrange
        Department department = new Department();
        department.setIsClosed(true);
        Person person = new Person();
        DepartmentFullResponse departmentFullResponse = new DepartmentFullResponse();
        when(departmentService.getById(anyInt())).thenReturn(departmentFullResponse);
        when(departmentMapper.map(departmentFullResponse)).thenReturn(department);
        when(personRepository.findById(anyInt())).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // Act
        personService.addPersonToDepartment(1, 1);

        // Assert
        verify(departmentService, times(1)).getById(anyInt());
        verify(personRepository, times(1)).findById(anyInt());
        verify(departmentMapper, times(1)).map(any(DepartmentFullResponse.class));
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void removePersonFromDepartment() {
        // Arrange
        Department department = new Department();
        Person person = new Person();
        DepartmentFullResponse departmentFullResponse = new DepartmentFullResponse();
        when(departmentService.getById(anyInt())).thenReturn(departmentFullResponse);
        when(departmentMapper.map(departmentFullResponse)).thenReturn(department);
        when(personRepository.findById(anyInt())).thenReturn(Optional.of(person));

        // Act
        personService.removePersonFromDepartment(1, 1);

        // Assert
        verify(departmentService, times(1)).getById(anyInt());
        verify(personRepository, times(1)).findById(anyInt());
        verify(departmentMapper, times(1)).map(any(DepartmentFullResponse.class));
        verify(personRepository, times(1)).save(any(Person.class));
    }
}
