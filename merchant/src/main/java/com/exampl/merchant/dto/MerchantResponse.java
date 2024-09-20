package com.exampl.merchant.dto;
import java.time.LocalDateTime;

public record MerchantResponse(
        Long merchantId,
        String businessName,
        String businessAddress,
        Long contactNumber,
        String email,
        Long userId,
        LocalDateTime createDate,
        LocalDateTime lastModified
) {
}
