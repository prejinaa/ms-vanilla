package com.example.Account.controller;

import com.example.Account.dto.AccountCreationRequest;
import com.example.Account.dto.AccountResponse;
import com.example.Account.exception.CustomerNotFoundException;
import com.example.Account.exception.MerchantNotFoundException;
import com.example.Account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping()
    ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreationRequest accountCreationRequest) {
        log.info("Received request to create an account for Merchant ID: {} and Customer ID: {}",
                accountCreationRequest.merchantId(), accountCreationRequest.customerId());

        AccountResponse accountResponse = accountService.createAccount(accountCreationRequest);
        log.info("Account created successfully with Account ID: {}", accountResponse.accountId());

        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) {
        log.info("Fetching account details for Account ID: {}", accountId);

        AccountResponse account = accountService.getAccountById(accountId);
        log.info("Returning account details for Account ID: {}", accountId);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/merchant/{merchantId}")
    ResponseEntity<List<AccountResponse>> getAccountByMerchantId(@PathVariable Long merchantId) throws MerchantNotFoundException {
        log.info("Fetching accounts for Merchant ID: {}", merchantId);

        List<AccountResponse> accountResponse = accountService.getAccountByMerchantId(merchantId);
        log.info("Returning {} accounts for Merchant ID: {}", accountResponse.size(), merchantId);

        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    ResponseEntity<List<AccountResponse>> getAccountByCustomerId(@PathVariable Long customerId) throws CustomerNotFoundException {
        log.info("Fetching accounts for Customer ID: {}", customerId);
        List<AccountResponse> accountResponse = accountService.getAccountByCustomerById(customerId);

        log.info("Returning {} accounts for Customer ID: {}", accountResponse.size(), customerId);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @DeleteMapping("{accountId}")
    ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        log.info("Received request to delete account with Account ID: {}", accountId);

        accountService.deleteAccount(accountId);
        log.info("Account with ID: {} deleted successfully", accountId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
