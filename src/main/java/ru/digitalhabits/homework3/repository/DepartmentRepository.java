package ru.digitalhabits.homework3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.exception.DepartmentNotFoundException;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    default Department getById(int id) {
        return this.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
    }
}
