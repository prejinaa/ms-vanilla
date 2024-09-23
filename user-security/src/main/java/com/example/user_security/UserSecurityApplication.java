package com.example.user_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserSecurityApplication.class, args);
	}

}
