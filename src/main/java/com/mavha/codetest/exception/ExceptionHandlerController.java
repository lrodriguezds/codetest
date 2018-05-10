package com.mavha.codetest.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler that will map application exception to http errors
 *
 */
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> defaultErrorHandler(Exception ex) throws Exception {

        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        response.setErrorMessage(ex.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PersonAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> personAlreadyExists(PersonAlreadyExistsException ex) {

        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        response.setErrorMessage(ex.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ExceptionResponse> personNotFound(PersonNotFoundException ex) {

        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.NOT_FOUND.toString());
        response.setErrorMessage(ex.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgument(IllegalArgumentException ex) {

        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        response.setErrorMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());

        response.setErrors(Arrays.asList(ex.getMessage()));

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handling for bean validations
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> invalidInput(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        response.setErrorMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());

        List<String> errors = new ArrayList<>();
        result.getAllErrors().forEach(e -> errors.add(e.getDefaultMessage()));
        response.setErrors(errors);

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }
}