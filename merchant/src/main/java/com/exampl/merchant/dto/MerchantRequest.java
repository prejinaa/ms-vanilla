package com.exampl.merchant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MerchantRequest(
        @NotBlank(message = "Business Name must be provided")
        @Size(max = 100, message = "Business Name must not exceed 100 characters")
        String businessName,

        @NotBlank(message = "Business Address must be provided")
        @Size(max = 255, message = "Business Address must not exceed 255 characters")
        String businessAddress,

        @NotNull(message = "Contact Number must be provided")
        @Pattern(regexp = "^\\d{10}$", message = "Contact Number must be a 10-digit number")
        String contactNumber, // Changed to String

        @NotBlank(message = "Email must be provided")
        @Email(message = "Email must be valid")
        String email,
        Integer createdBy
) {
}
