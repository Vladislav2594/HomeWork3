package ru.digitalhabits.homework3.exception;

public class DepartmentNotFoundException extends IllegalArgumentException {

    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
