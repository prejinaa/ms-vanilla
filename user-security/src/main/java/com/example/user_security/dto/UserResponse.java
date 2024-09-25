package com.example.user_security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
// Add other fields as necessary
}
