package com.example.customer.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CustomerResponseWithAccount(

        Long customerId,
        String name,
        Long contactNumber,
        String address,
        String email,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        Integer createdBy,
        Integer lastModifiedBy,
        List<AccountResponse> accountResponse
) {
}
