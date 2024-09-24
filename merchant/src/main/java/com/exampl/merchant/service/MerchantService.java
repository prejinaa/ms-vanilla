package com.exampl.merchant.service;
import com.exampl.merchant.dto.AccountResponse;
import com.exampl.merchant.dto.MerchantRequest;
import com.exampl.merchant.dto.MerchantResponse;
import com.exampl.merchant.dto.MerchantWithAccountResponse;
import com.exampl.merchant.exception.MerchantNotFound;
import com.exampl.merchant.model.Merchant;
import com.exampl.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final WebClient webClient;


    public List<MerchantResponse> getALlMerchant() {
        log.info("Fetching all merchants...");
        List<MerchantResponse> merchants = merchantRepository.findAllBy();
        log.info("Fetched {} merchants", merchants.size());
        return merchants;
    }

    public MerchantResponse createMerchant(MerchantRequest merchantRequest) {
        log.info("Creating a new merchant with business name: {}", merchantRequest.businessName());
        Merchant merchant = new Merchant();
        merchant.setBusinessName(merchantRequest.businessName());
        merchant.setBusinessAddress(merchantRequest.businessAddress());
        merchant.setContactNumber(merchantRequest.contactNumber());
        merchant.setEmail(merchantRequest.email());


        Merchant savedMerchant = merchantRepository.save(merchant);
        log.info("Merchant created with ID: {}", savedMerchant.getMerchantId());

        return new MerchantResponse(
                savedMerchant.getMerchantId(),
                savedMerchant.getBusinessName(),
                savedMerchant.getBusinessAddress(),
                savedMerchant.getContactNumber(),
                savedMerchant.getEmail(),

                savedMerchant.getCreateDate(),
                savedMerchant.getLastModified(),
                savedMerchant.getCreatedBy(),
                savedMerchant.getLastModifiedBy()
        );
    }

    public MerchantWithAccountResponse getMerchantById(Long merchantId) throws MerchantNotFound {
        log.info("Fetching merchant by ID: {}", merchantId);
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> {
                    log.error("Merchant not found with ID: {}", merchantId);
                    return new MerchantNotFound(merchantId);
                });

        log.info("Fetching accounts for merchant ID: {}", merchantId);
        List<AccountResponse> accountResponse = webClient.get()
                .uri("http://localhost:8083/api/accounts/merchant/" + merchantId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AccountResponse>>() {})
                .onErrorResume(e -> {
                    log.error("Error fetching accounts for merchant ID: {}: {}", merchantId, e.getMessage());
                    return Mono.empty();
                })
                .block();

        log.info("Returning merchant and account information for merchant ID: {}", merchantId);
        return new MerchantWithAccountResponse(
                merchant.getMerchantId(),
                merchant.getBusinessName(),
                merchant.getBusinessAddress(),
                merchant.getContactNumber(),
                merchant.getEmail(),

                merchant.getCreateDate(),
                merchant.getLastModified(),
                merchant.getCreatedBy(),
                merchant.getLastModifiedBy(),
                accountResponse
        );
    }

    public MerchantResponse updateMerchant(MerchantRequest merchantRequest, Long merchantId) {
        log.info("Updating merchant with ID: {}", merchantId);
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> {
                    log.error("Merchant not found with ID: {}", merchantId);
                    return new MerchantNotFound(merchantId);
                });

        merchant.setBusinessName(merchantRequest.businessName());
        merchant.setBusinessAddress(merchantRequest.businessAddress());
        merchant.setContactNumber(merchantRequest.contactNumber());
        merchant.setEmail(merchantRequest.email());


        Merchant updatedMerchant = merchantRepository.save(merchant);
        log.info("Merchant updated with ID: {}", updatedMerchant.getMerchantId());

        return new MerchantResponse(
                updatedMerchant.getMerchantId(),
                updatedMerchant.getBusinessName(),
                updatedMerchant.getBusinessAddress(),
                updatedMerchant.getContactNumber(),
                updatedMerchant.getEmail(),
                updatedMerchant.getCreateDate(),
                updatedMerchant.getLastModified(),
                updatedMerchant.getCreatedBy(),
                updatedMerchant.getLastModifiedBy()
        );
    }

//    public List<MerchantWithAccountResponse> getMerchantByUserID(Long userId) {
//        log.info("Fetching merchants for user ID: {}", userId);
//        List<Merchant> merchants = merchantRepository.findByUserId(userId);
//
//        if (merchants.isEmpty()) {
//            log.error("No merchants found for user ID: {}", userId);
//            throw new RuntimeException("No merchants found for userId: " + userId);
//        }
//
//        return merchants.stream()
//                .map(merchant -> {
//                    log.info("Fetching accounts for merchant ID: {}", merchant.getMerchantId());
//                    List<AccountResponse> accountResponses = webClient.get()
//                            .uri("http://localhost:8083/api/accounts/merchant/" + merchant.getMerchantId())
//                            .retrieve()
//                            .bodyToMono(new ParameterizedTypeReference<List<AccountResponse>>() {})
//                            .onErrorResume(e -> {
//                                log.error("Error fetching accounts for merchant ID: {}: {}", merchant.getMerchantId(), e.getMessage());
//                                return Mono.empty();
//                            })
//                            .block();
//
//                    return new MerchantWithAccountResponse(
//                            merchant.getMerchantId(),
//                            merchant.getBusinessName(),
//                            merchant.getBusinessAddress(),
//                            merchant.getContactNumber(),
//                            merchant.getEmail(),
//                            merchant.getUserId(),
//                            merchant.getCreateDate(),
//                            merchant.getLastModified(),
//                            merchant.getCreatedBy(),
//                            merchant.getLastModifiedBy(),
//                            accountResponses
//                    );
//                })
//                .collect(Collectors.toList());
//    }
    public void deleteMerchant(Long merchantId){
        log.info("Received request to delete Merchant with Merchant ID: {}", merchantId);

        if (!merchantRepository.existsById(merchantId)) {
            log.error("Merchant not found with ID: {}", merchantId);
            throw new MerchantNotFound(merchantId);
        }

        merchantRepository.deleteById(merchantId);
        log.info("Merchant with ID: {} deleted successfully", merchantId);
    }

}

