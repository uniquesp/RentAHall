package com.rentahall.userservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register").permitAll() // allow public registration
                        .requestMatchers("/api/users/all", "/api/users/email/**").authenticated() // protect other endpoints
                )
                .httpBasic(Customizer.withDefaults()); // enable HTTP Basic auth (optional)

        return http.build();
    }
}