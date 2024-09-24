package com.example.user_security.dto;

import java.time.LocalDateTime;

public record MerchantWithAccountResponse(
        Long merchantId,
        String businessName,
        String businessAddress,
        Long contactNumber,
        String email,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        Integer createdBy,
        Integer lastModifiedBy,
        java.util.List<AccountResponse> accountResponse) {
}
