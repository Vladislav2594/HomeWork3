package ru.digitalhabits.homework3.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.exception.DepartmentNotFoundException;
import ru.digitalhabits.homework3.mapper.DepartmentMapper;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.model.DepartmentShortResponse;
import ru.digitalhabits.homework3.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        // Given
        List<Department> departments = List.of(new Department(), new Department());
        List<DepartmentShortResponse> responses = List.of(new DepartmentShortResponse(), new DepartmentShortResponse());

        when(departmentRepository.findAll()).thenReturn(departments);
        when(departmentMapper.mapToShort(departments)).thenReturn(responses);

        // When
        List<DepartmentShortResponse> result = departmentService.findAll();

        // Then
        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findAll();
        verify(departmentMapper, times(1)).mapToShort(departments);
    }

    @Test
    void findById() {
        // Given
        int departmentId = 1;
        Department department = new Department();
        DepartmentFullResponse response = new DepartmentFullResponse();

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentMapper.map(department)).thenReturn(response);

        // When
        DepartmentFullResponse result = departmentService.getById(departmentId);

        // Then
        assertNotNull(result);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentMapper, times(1)).map(department);
    }

    @Test
    void findById_DepartmentNotFound() {
        // Given
        int departmentId = 1;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Then
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getById(departmentId));
        verify(departmentRepository, times(1)).findById(departmentId);
    }


    @Test
    void create() {
        // Given
        DepartmentRequest request = new DepartmentRequest();
        Department department = new Department();
        department.setId(1);

        when(departmentMapper.map(request)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);

        // When
        int result = departmentService.create(request);

        // Then
        assertEquals(1, result);
        verify(departmentMapper, times(1)).map(request);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void update() {
        // Given
        int departmentId = 1;
        DepartmentRequest request = new DepartmentRequest();
        Department department = new Department();
        DepartmentFullResponse response = new DepartmentFullResponse();

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentMapper.map(response)).thenReturn(department);
        when(departmentMapper.map(department, request)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.map(department)).thenReturn(response);

        // When
        DepartmentFullResponse result = departmentService.update(departmentId, request);

        // Then
        assertNotNull(result);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentMapper, times(1)).map(department, request);
        verify(departmentRepository, times(1)).save(department);
        verify(departmentMapper, times(2)).map(department);
    }

    @Test
    void update_DepartmentNotFound() {
        // Given
        int departmentId = 1;
        DepartmentRequest request = new DepartmentRequest();
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Then
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.update(departmentId, request));
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void delete() {
        // Given
        int departmentId = 1;

        // When
        departmentService.delete(departmentId);

        // Then
        verify(departmentRepository, times(1)).deleteById(departmentId);
    }

    @Test
    void close() {
        // Given
        int departmentId = 1;
        Department department = new Department();
        DepartmentFullResponse departmentFullResponse = new DepartmentFullResponse();

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentMapper.map(departmentFullResponse)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);

        // When
        departmentService.close(departmentId);

        // Then
        assertTrue(department.getIsClosed());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void close_DepartmentNotFound() {
        // Given
        int departmentId = 1;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Then
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.close(departmentId));
        verify(departmentRepository, times(1)).findById(departmentId);
    }
}
