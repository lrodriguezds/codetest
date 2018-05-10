package com.mavha.codetest.exception;

/**
 * Specific exception used to inform a if a person does not exist.
 *
 */
public class PersonNotFoundException extends RuntimeException {

    private Integer dni;

    public PersonNotFoundException(Integer dni, String message) {
        super(message);
        this.dni = dni;
    }
}