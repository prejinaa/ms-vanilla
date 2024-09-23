package com.example.user_security.exception;

import lombok.Getter;

@Getter
public class UserNotFound extends RuntimeException {
    private final Long userId;
    public UserNotFound(Long userId){
        super("User with userId"+ userId+ " not found");
        this.userId=userId;
    }

}
