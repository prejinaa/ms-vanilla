package com.example.user_security.dto;

import java.time.LocalDateTime;

public record MerchantWithAccountResponse(
        Long merchantId,
        String businessName,
        String businessAddress,
        Long contactNumber,
        String email,
        Long userId,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        java.util.List<AccountResponse> accountResponse) {
}
