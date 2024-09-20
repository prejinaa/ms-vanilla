package com.exampl.merchant.controller;
import com.exampl.merchant.dto.MerchantRequest;
import com.exampl.merchant.dto.MerchantResponse;
import com.exampl.merchant.dto.MerchantWithAccountResponse;
import com.exampl.merchant.service.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
@Slf4j
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping()
    private ResponseEntity<List<MerchantResponse>> getALlMerchant() {
        log.info("Received request to fetch all merchants.");
        List<MerchantResponse> merchants = merchantService.getALlMerchant();
        log.info("Returning {} merchants.", merchants.size());
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    @PostMapping()
    private ResponseEntity<MerchantResponse> createMerchant(@RequestBody MerchantRequest merchantRequest) {
        log.info("Received request to create a new merchant with business name: {}", merchantRequest.businessName());
        MerchantResponse merchantResponse = merchantService.createMerchant(merchantRequest);
        log.info("Merchant created successfully with ID: {}", merchantResponse.merchantId());
        return new ResponseEntity<>(merchantResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{merchantId}")
    private ResponseEntity<MerchantWithAccountResponse> getMerchantById(@PathVariable Long merchantId) {
        log.info("Received request to fetch merchant with ID: {}", merchantId);
        MerchantWithAccountResponse merchantWithAccountResponse = merchantService.getMerchantById(merchantId);
        log.info("Returning merchant details for merchant ID: {}", merchantId);
        return new ResponseEntity<>(merchantWithAccountResponse, HttpStatus.OK);
    }

    @PutMapping("/{merchantId}")
    private ResponseEntity<MerchantResponse> updateMerchant(@RequestBody MerchantRequest merchantRequest, @PathVariable Long merchantId) {
        log.info("Received request to update merchant with ID: {}", merchantId);
        MerchantResponse merchantResponse = merchantService.updateMerchant(merchantRequest, merchantId);
        log.info("Merchant with ID: {} updated successfully.", merchantId);
        return new ResponseEntity<>(merchantResponse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<List<MerchantWithAccountResponse>> getMerchantByUserID(@PathVariable Long userId) {
        log.info("Received request to fetch merchants for user ID: {}", userId);
        List<MerchantWithAccountResponse> merchantResponses = merchantService.getMerchantByUserID(userId);
        log.info("Returning {} merchants for user ID: {}", merchantResponses.size(), userId);
        return new ResponseEntity<>(merchantResponses, HttpStatus.OK);
    }
    @DeleteMapping("{merchantId}")
    ResponseEntity<?> deleteAccount(@PathVariable Long merchantId) {
        log.info("Received request to delete merchant with Merchant ID: {}", merchantId);

        merchantService.deleteMerchant(merchantId);
        log.info("Merchant with ID: {} deleted successfully", merchantId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
