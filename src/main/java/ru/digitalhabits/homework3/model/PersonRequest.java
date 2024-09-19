package ru.digitalhabits.homework3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {

    @NotEmpty(message = "{field.is.empty}")
    private String firstName;

    private String middleName;

    @NotEmpty(message = "{field.is.empty}")
    private String lastName;

    @Min(value = 18, message = "{field.min.value}")
    @Max(value = 65, message = "{field.max.value}")
    private Integer age;
}
