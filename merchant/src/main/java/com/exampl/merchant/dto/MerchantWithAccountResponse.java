package com.exampl.merchant.dto;
import java.time.LocalDateTime;

public record MerchantWithAccountResponse(
        Long merchantId,
        String businessName,
        String businessAddress,
        String contactNumber,
        String email,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        Integer createdBy,
        java.util.List<AccountResponse> accountResponse) {
}
