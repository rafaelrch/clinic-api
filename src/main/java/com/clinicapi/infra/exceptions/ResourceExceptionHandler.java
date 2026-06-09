package com.clinicapi.infra.exceptions;

import com.clinicapi.domain.service.exceptions.BusinessRuleException;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> businessRule(BusinessRuleException e, HttpServletRequest request) {
        String error = "Business rule violation";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> fieldError(MethodArgumentNotValidException e, HttpServletRequest request) {

        List<FieldErrorMessage> erros = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldErrorMessage(fe.getField(), fe.getDefaultMessage()))
                .toList();

        String error = "Validation error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationErrorResponse response = new ValidationErrorResponse(Instant.now(), status.value(), error, request.getRequestURI(), erros);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrity(DataIntegrityViolationException e, HttpServletRequest request){
        String error = "Conflict";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error, "This operation can't be completed because the resource is linked to the other record", request.getRequestURI());

        return  ResponseEntity.status(status).body(err);
    }

}
