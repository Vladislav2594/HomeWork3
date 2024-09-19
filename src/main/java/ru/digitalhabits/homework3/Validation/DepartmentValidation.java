package ru.digitalhabits.homework3.Validation;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import ru.digitalhabits.homework3.domain.Department;

import javax.validation.ValidationException;

@Component
public class DepartmentValidation {

    public void validate(Department department) {
        if (ObjectUtils.isEmpty(department)) {
            return;
        }
        if (ObjectUtils.notEqual(department.getIsClosed(), false)) {
            throw new ValidationException("Department is closed");
        }
    }
}
