package com.example.customer.exception;

import lombok.Getter;

@Getter
public class CustomerNotFoundWithUser extends RuntimeException {
    private final Long userId;

    public CustomerNotFoundWithUser(Long userId) {
        super("Customer with UserId" + userId + " Not Found");
        this.userId = userId;
    }
}
