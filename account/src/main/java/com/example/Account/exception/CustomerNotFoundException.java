package com.example.Account.exception;

import lombok.Getter;

@Getter
public class CustomerNotFoundException extends RuntimeException{
    private final Long customerId;
    public CustomerNotFoundException (Long customerId) {
        super("Merchant with Id " + customerId+ " not found");
        this.customerId= customerId;
    }
}
