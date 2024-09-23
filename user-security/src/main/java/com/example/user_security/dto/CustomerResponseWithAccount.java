package com.example.user_security.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CustomerResponseWithAccount(

        Long customerId,
        String name,
        Long contactNumber,
        String address,
        String email,
        Long userId,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        List<AccountResponse> accountResponse
) {
}