package com.example.Account.dto;
import com.example.Account.model.AccountType;

import java.time.LocalDateTime;

public record AccountResponse(

        Long accountId,
        String accountNumber,
        Double balance,
        AccountType accountType,
        LocalDateTime createDate
)
  {
  }
