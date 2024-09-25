package com.example.user_security.controller;

import com.example.user_security.dto.*;
import com.example.user_security.model.User;
import com.example.user_security.repo.UserRepository;
import com.example.user_security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    private final UserRepository repository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthResponse> authentication(@RequestBody AuthRequest request) throws Exception {
        return ResponseEntity.ok(service.authenticate(request));
    }

//   @GetMapping("/current/id")
//      public Optional<Integer> getCurrentUserId() {
//             return applicationAuditAware.getCurrentAuditor();
//   }

    @GetMapping("/{userId}/{merchant}")
    public Mono<ResponseEntity<List<MerchantResponse>>> getMerchantsByUserId(
            @PathVariable Long userId,
            Authentication authentication) {
        String jwtToken = service.extractTokenFromAuthentication(authentication);
        return service.getAllMerchants(jwtToken)
                .map(merchants -> ResponseEntity.ok(merchants))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }


    @GetMapping("/merchants/{merchantId}")
    public Mono<ResponseEntity<MerchantWithAccountResponse>> getMerchantById(
            @PathVariable Long merchantId,
            Authentication authentication) {
        String jwtToken = service.extractTokenFromAuthentication(authentication);

        return service.getMerchantById(merchantId, jwtToken)
                .map(merchant -> ResponseEntity.ok(merchant))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
//
//    @PostMapping("/merchants")
//    public ResponseEntity<MerchantResponse> createMerchant(
//            @RequestBody MerchantRequest merchantRequest,
//            Authentication authentication) {
//
//        String jwtToken = service.extractTokenFromAuthentication(authentication);//TODO:refractor it make separte method to get the jwt
//
//        Object principal = authentication.getPrincipal().;
//
//        if (principal instanceof UserReponse) {
//           merchantRequest=new MerchantRequest(merchantRequest.businessName(), merchantRequest.businessAddress(),merchantRequest.email(),merchantRequest.contactNumber(),merchantRequest.createdBy());
//            // Cast to your custom DTO
//        }
//        return service.createMerchant(merchantRequest, jwtToken)
//                .map(merchant -> ResponseEntity.status(HttpStatus.CREATED).body(merchant))
//                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()).block();
//    }
//}


    @PostMapping("/merchants")
    public ResponseEntity<MerchantResponse> createMerchant(
            @RequestBody MerchantRequest merchantRequest,
            Authentication authentication) {

        String jwtToken = extractJwtFromAuthentication(authentication);

        Object useEmail = authentication.getName();

        if ( useEmail!=null) {


            User user=repository.findByEmail((String) useEmail).get();
            // Create MerchantRequest with the user's ID
            merchantRequest = new MerchantRequest(
                    merchantRequest.businessName(),
                    merchantRequest.businessAddress(),
                    merchantRequest.email(),
                    merchantRequest.contactNumber(),
                  user.getId()// Set the user ID as createdBy
            );
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return service.createMerchant(merchantRequest, jwtToken)
                .map(merchant -> ResponseEntity.status(HttpStatus.CREATED).body(merchant))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()).block();
    }

    // Method to extract JWT from Authentication
    private String extractJwtFromAuthentication(Authentication authentication) {
        // Assuming you have a method to get the JWT from the authentication
        // Here, it's a placeholder; adjust it based on your implementation
        return service.extractTokenFromAuthentication(authentication);
    }


}