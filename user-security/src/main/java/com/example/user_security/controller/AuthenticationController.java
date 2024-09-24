package com.example.user_security.controller;
import com.example.user_security.dto.*;
import com.example.user_security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthResponse> authentication(@RequestBody AuthRequest request) throws Exception {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @GetMapping("/current")
    public Mono<UserResponse> getCurrentUser(Authentication authentication) {
        return Mono.just(service.getCurrentUser(authentication));
    }
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

        // Extract JWT token from authentication
        String jwtToken = service.extractTokenFromAuthentication(authentication);

        return service.getMerchantById(merchantId, jwtToken)
                .map(merchant -> ResponseEntity.ok(merchant))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }




}
