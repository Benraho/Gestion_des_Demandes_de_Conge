package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.dto.LoginRequestDTO;
import com.example.MiniProject.application.service.AuthService;
import com.example.MiniProject.domain.model.Utilisateur;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody Utilisateur utilisateur) {
        return authService.register(utilisateur);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussite"),
            @ApiResponse(responseCode = "403", description = "Utilisateur non trouvé ou mot de passe incorrect")
    })
    public String login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }
}