package com.example.customer.dto;

import java.time.LocalDateTime;

public record CustomerResponse(

        Long customerId,
        String name,
        Long contactNumber,
        String address,
        String email,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        Integer createdBy,
        Integer lastModifiedBy


) {
}
