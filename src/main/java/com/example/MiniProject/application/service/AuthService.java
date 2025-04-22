package com.example.MiniProject.application.service;

import com.example.MiniProject.application.dto.LoginRequestDTO;
import com.example.MiniProject.domain.model.Role;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import com.example.MiniProject.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;

    public AuthService(UtilisateurRepository utilisateurRepository, JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtService = jwtService;
    }

    public String register(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(utilisateur.getMotDePasse()));
        utilisateur.setRole(Role.EMPLOYE);
        utilisateurRepository.save(utilisateur);
        return "Inscription réussite";
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        Utilisateur user = utilisateurRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (new BCryptPasswordEncoder().matches(loginRequestDTO.getMotDePasse(), user.getMotDePasse())) {
            return jwtService.genrateToken(user.getEmail());
        }

        throw new RuntimeException("Mot de passe incorrect");
    }
}