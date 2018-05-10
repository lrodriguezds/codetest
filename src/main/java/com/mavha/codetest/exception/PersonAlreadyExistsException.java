package com.mavha.codetest.exception;

/**
 * Specific exception used to inform a if a person to create already exists.
 *
 */
public class PersonAlreadyExistsException extends RuntimeException {

    private Integer dni;

    public PersonAlreadyExistsException(Integer dni, String message) {
        super(message);
        this.dni = dni;
    }
}