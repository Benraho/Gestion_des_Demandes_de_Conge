package com.example.MiniProject.presentation.controller;


import com.example.MiniProject.application.dto.LoginRequestDTO;
import com.example.MiniProject.domain.model.Role;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import com.example.MiniProject.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;

    public AuthController(UtilisateurRepository utilisateurRepository, JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@RequestBody Utilisateur utilisateur){
        utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(utilisateur.getMotDePasse()));
        utilisateur.setRole(Role.EMPLOYE);
        utilisateurRepository.save(utilisateur);
        return "Inscription réussite";
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Connexion réussite"),
            @ApiResponse(responseCode = "403" , description = "Utilisateur non trouvé ou mot de passe incorrect")
    })
    public String login(@RequestBody LoginRequestDTO loginRequestDTO){
        Utilisateur user = utilisateurRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(()-> new RuntimeException("Utilisateur non trouvé"));
        if (new BCryptPasswordEncoder().matches(loginRequestDTO.getMotDePasse(),user.getMotDePasse())){
            return jwtService.genrateToken(user.getEmail());
        }
        throw  new RuntimeException("Mot de passe incorrect");
    }
}
