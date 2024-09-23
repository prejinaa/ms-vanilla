package com.example.user_security.controller;

import com.example.user_security.dto.*;
import com.example.user_security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/merchants/{userId}")
    public ResponseEntity<List<MerchantResponse>> getMerchantsByUserId(@PathVariable Long userId) {
        List<MerchantResponse> merchants = service.MerchantController();
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }
}
