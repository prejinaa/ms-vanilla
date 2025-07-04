package com.example.user_security.dto;
import java.time.LocalDateTime;

public record MerchantResponse(
        Long merchantId,
        String businessName,
        String businessAddress,
        String contactNumber,
        String email,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        Integer createdBy,
        Integer lastModifiedBy

) {
}
