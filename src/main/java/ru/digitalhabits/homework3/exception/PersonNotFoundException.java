package ru.digitalhabits.homework3.exception;

public class PersonNotFoundException extends IllegalArgumentException {

    public PersonNotFoundException(String message) {
        super(message);
    }
}
