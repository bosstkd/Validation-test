package com.mnb.projet.presentation.common.error.adapter;

import com.mnb.projet.domain.common.exceptions.DomainBadRequestCommandException;
import com.mnb.projet.domain.common.exceptions.DomainResourceNotFoundException;
import com.mnb.projet.domain.validation.exception.ErrorMessage;
import com.mnb.projet.domain.validation.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class MnbExceptionAdvicer {

    @ExceptionHandler(DomainResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> MnbNotFoundException(DomainResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                ex.getDetails());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DomainBadRequestCommandException.class, ValidationException.class})
    public ResponseEntity<ErrorMessage> badRequestExceptionHandler(RuntimeException ex, WebRequest request) {

        if (ex instanceof DomainBadRequestCommandException) {
            DomainBadRequestCommandException exception = (DomainBadRequestCommandException) ex;
            ErrorMessage message = new ErrorMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    new Date(),
                    exception.getMessage(),
                    exception.getDetails());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (ex instanceof ValidationException) {
            ValidationException exception = (ValidationException) ex;
            Object details = exception.getErreurs();
            ErrorMessage message = new ErrorMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    new Date(),
                    exception.getMessage(),
                    details);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return globalExceptionHandler(ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                null);

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
