package com.example.user_security.service;

import com.example.user_security.config.JwtService;
import com.example.user_security.dto.*;
import com.example.user_security.model.Role;
import com.example.user_security.model.User;
import com.example.user_security.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final WebClient webClient;

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MERCHANT)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("Invalid user"));

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }


public Mono<List<MerchantResponse>> getAllMerchants(String jwtToken) {
    return webClient.get()
            .uri("http://localhost:8084/api/merchant")
            .header("Authorization", "Bearer " + jwtToken)  // Include JWT token
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<MerchantResponse>>() {})
            .onErrorResume(e -> {
                // Handle errors
                return Mono.empty();
            });
}



    public Mono<MerchantResponse> createMerchant(MerchantRequest merchantRequest, String jwtToken) {
        Mono<MerchantResponse> merchantResponseMono=    webClient.post()
                .uri("http://localhost:8084/api/merchant")
                .header("Authorization", "Bearer " + jwtToken)
                .bodyValue(merchantRequest)
                .retrieve()
                .bodyToMono(MerchantResponse.class)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
        return merchantResponseMono;
    }

    public Mono<MerchantWithAccountResponse> getMerchantById(Long merchantId,String jwtToken) {
        return webClient.get()
                .uri("http://localhost:8084/api/merchant/{merchantId}", merchantId)
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(MerchantWithAccountResponse.class)
                .onErrorResume(e -> {
                    // Handle errors
                    return Mono.empty();
                });
    }


    public String extractTokenFromAuthentication(Authentication authentication) {

        return (String) authentication.getCredentials();
    }


    }