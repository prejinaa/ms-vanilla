package com.example.Account.dto;

import com.example.Account.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccountCreationRequest (

        @NotBlank(message = "Account Number must be provided")
        String accountNumber,

        @NotNull(message = "Balance must be provided")
        @Positive(message = "Balance must be a positive value")
        Double balance,

        @NotNull(message = "Account Type must be provided")
        AccountType accountType,

        Long merchantId,

        Long customerId
) {
}
