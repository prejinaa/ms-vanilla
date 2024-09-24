package com.example.user_security.dto;

import jakarta.validation.constraints.*;

public record MerchantRequest(
        @NotBlank(message = "Business Name must be provided")
        @Size(max = 100, message = "Business Name must not exceed 100 characters")
        String businessName,

        @NotBlank(message = "Business Address must be provided")
        @Size(max = 255, message = "Business Address must not exceed 255 characters")
        String businessAddress,

        @NotNull(message = "Contact Number must be provided")
        @Pattern(regexp = "^\\d{10}$", message = "Contact Number must be a 10-digit number")
        String contactNumber,

        @NotBlank(message = "Email must be provided")
        @Email(message = "Email must be valid")
        String email
) {
}
