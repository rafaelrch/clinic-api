package com.clinicapi.infra.exceptions;

import com.clinicapi.domain.service.exceptions.BusinessRuleException;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
     public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
     }

     @ExceptionHandler(BusinessRuleException.class)
     public ResponseEntity<StandardError> businessRule(BusinessRuleException e, HttpServletRequest request){
         String error = "Business rule violation";
         HttpStatus status = HttpStatus.BAD_REQUEST;
         StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

         return ResponseEntity.status(status).body(err);
     }

}
