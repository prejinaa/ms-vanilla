package com.example.Account.exception;
import lombok.Getter;

@Getter
public class MerchantNotFoundException extends RuntimeException {
    private final Long merchantId;

    public MerchantNotFoundException( Long merchantId) {
        super("Merchant with Id " + merchantId + " not found");
        this.merchantId = merchantId;

    }
}
