package ru.digitalhabits.homework3.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "department_name")
    private String name;

    @Column(name = "is_closed")
    private Boolean isClosed;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Person> personList;
}