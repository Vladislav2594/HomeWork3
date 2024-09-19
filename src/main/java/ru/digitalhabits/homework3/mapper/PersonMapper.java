package ru.digitalhabits.homework3.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.model.PersonFullResponse;
import ru.digitalhabits.homework3.model.PersonRequest;
import ru.digitalhabits.homework3.model.PersonShortResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonShortResponse mapToShort(Person person);

    List<PersonShortResponse> mapToShort(List<Person> persons);

    PersonFullResponse map(Person person);

    @Mapping(target = "fullName", expression = "java(String.format(\"%s %s %s\", request.getFirstName(), request.getMiddleName(), request.getLastName()))")
    Person map(PersonRequest request);
    Person map(PersonFullResponse request);

    @Mapping(target = "fullName", expression = "java(String.format(\"%s %s %s\", request.getFirstName(), request.getMiddleName(), request.getLastName()))")
    Person map(@MappingTarget Person person, PersonRequest request);
}
