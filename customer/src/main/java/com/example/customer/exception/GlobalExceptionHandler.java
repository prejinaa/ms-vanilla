package com.example.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFound(CustomerNotFoundException exception) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("error", "Customer Not Found");
        responseBody.put("message", exception.getMessage());
        responseBody.put("customer", exception.getCustomerId());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundWithUser.class)
    public ResponseEntity<Object> handleCustomerNotFoundWithUser(CustomerNotFoundWithUser exception) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("error", "Customer Not Found");
        responseBody.put("message", exception.getMessage());
        responseBody.put("customer", exception.getUserId());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }
}

