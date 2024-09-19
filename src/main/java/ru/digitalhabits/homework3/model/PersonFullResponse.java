package ru.digitalhabits.homework3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PersonFullResponse {
    private Integer id;
    private String fullName;
    private Integer age;
    private DepartmentShortResponse department;
}
