package com.example.customer.exception;

import lombok.Getter;

@Getter
public class CustomerNotFoundException extends RuntimeException {
    private final Long customerId;
    public CustomerNotFoundException(Long customerId) {
        super("Customer with customerId" + customerId + " Not Found");
        this.customerId = customerId;

    }
}
