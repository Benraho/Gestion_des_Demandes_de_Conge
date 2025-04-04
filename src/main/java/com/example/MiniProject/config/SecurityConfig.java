package com.example.MiniProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui.html",
                                "swagger-ui/**",
                                "v3/api-docs/**",
                                "/api/auth/**"
                        ).permitAll()
                        .requestMatchers("/api/conges/employe/**").hasRole("EMPLOYE")
                        .requestMatchers("/api/conges/approuver/**" , "/api/conges/refuser/**").hasRole("MANAGER")
                        .anyRequest().hasRole("ADMIN")

        )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
}
}
