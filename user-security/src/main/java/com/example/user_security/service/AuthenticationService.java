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

        // save user to db
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

        // find user from db
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("Invalid user"));

        // generate new token
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }


    public List<MerchantResponse> MerchantController() {
        return webClient.get()
                .uri("http://localhost:8084/api/merchant}")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MerchantResponse>>() {})
                .onErrorResume(e -> {
                    log.error("Error fetching merchant for user ID");
                    return Mono.empty();
                })
                .block();
    }

    }