package com.example.Account.service;
import com.example.Account.dto.AccountCreationRequest;
import com.example.Account.dto.AccountResponse;
import com.example.Account.exception.CustomerNotFoundException;
import com.example.Account.exception.MerchantNotFoundException;
import com.example.Account.model.Account;
import com.example.Account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Importing the logger
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // Lombok annotation for logging
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountResponse createAccount(AccountCreationRequest creationRequest) {
        log.info("Received request to create account for Merchant ID: {} and Customer ID: {}",
                creationRequest.merchantId(), creationRequest.customerId());
        Account account = new Account();
        account.setAccountNumber(creationRequest.accountNumber());
        account.setAccountType(creationRequest.accountType());
        account.setBalance(creationRequest.balance());
        account.setMerchantId(creationRequest.merchantId());
        account.setCustomerId(creationRequest.customerId());
        Account savedAccount = accountRepository.save(account);

        log.info("Account created successfully with Account ID: {}", savedAccount.getAccountId());
        return new AccountResponse(
                savedAccount.getAccountId(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                savedAccount.getAccountType(),
                savedAccount.getCreateDate()
        );
    }

    public AccountResponse getAccountById(Long accountId) {
        log.info("Fetching account details for Account ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found with ID: {}", accountId);
                    return new RuntimeException("Account with this ID not found");
                });
        log.info("Returning account details for Account ID: {}", accountId);
        return new AccountResponse(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getCreateDate()
        );
    }

    public List<AccountResponse> getAccountByMerchantId(Long merchantId) throws MerchantNotFoundException {
        log.info("Fetching accounts for Merchant ID: {}", merchantId);
        List<Account> accounts = accountRepository.findByMerchantId(merchantId);

        if (accounts.isEmpty()) {
            log.error("No accounts found for Merchant ID: {}", merchantId);
            throw new MerchantNotFoundException(merchantId);
        }
        log.info("Returning {} accounts for Merchant ID: {}", accounts.size(), merchantId);
        return accounts.stream()
                .map(a -> new AccountResponse(
                        a.getAccountId(),
                        a.getAccountNumber(),
                        a.getBalance(),
                        a.getAccountType(),
                        a.getCreateDate()))
                .collect(Collectors.toList());
    }

    public List<AccountResponse> getAccountByCustomerById(Long customerId) throws CustomerNotFoundException {
        log.info("Fetching accounts for Customer ID: {}", customerId);
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        if (accounts.isEmpty()) {
            log.error("No accounts found for Customer ID: {}", customerId);
            throw new CustomerNotFoundException(customerId);
        }
        log.info("Returning {} accounts for Customer ID: {}", accounts.size(), customerId);
        return accounts.stream()
                .map(account -> new AccountResponse(
                        account.getAccountId(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getAccountType(),
                        account.getCreateDate()))
                .collect(Collectors.toList());
    }

    public void deleteAccount(Long accountId) {
        log.info("Received request to delete account with Account ID: {}", accountId);
        if (!accountRepository.existsById(accountId)) {
            log.error("Account not found with ID: {}", accountId);
            throw new RuntimeException("Account not found");
        }
        accountRepository.deleteById(accountId);
        log.info("Account with ID: {} deleted successfully", accountId);
    }
}
