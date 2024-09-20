package com.example.customer.dto;
import jakarta.validation.constraints.*;

public record CustomerRequest(

        @NotBlank(message = " Name must be provided")
        @Size(max = 100, message = " Name must not exceed 100 characters")
        String name,

        @NotNull(message = "Contact Number must be provided")
        @Pattern(regexp = "^\\d{10}$", message = "Contact Number must be a 10-digit number")
        Long contactNumber,

        @NotBlank(message = "Address must be provided")
        @Size(max = 255, message = " Address must not exceed 255 characters")
        String address,

        @NotBlank(message = "Email must be provided")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "User ID must be provided")
        Long userId
        )
           {
             }
