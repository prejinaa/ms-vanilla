package com.example.customer.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long customerId,
        String name,
        Long contactNumber,
        String address,
        String email,
        Long userId,
        LocalDateTime createDate,
        LocalDateTime lastModified
) {
}
