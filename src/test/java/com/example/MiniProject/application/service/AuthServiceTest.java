package com.example.MiniProject.application.service;

import com.example.MiniProject.application.dto.LoginRequestDTO;
import com.example.MiniProject.domain.model.Role;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import com.example.MiniProject.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UtilisateurRepository utilisateurRepository;
    private JwtService jwtService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        utilisateurRepository = mock(UtilisateurRepository.class);
        jwtService = mock(JwtService.class);
        authService = new AuthService(utilisateurRepository, jwtService);
    }

    @Test
    void testRegisterSuccess() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("123456");

        String result = authService.register(utilisateur);

        assertEquals("Inscription réussite", result);
        assertEquals(Role.ROLE_EMPLOYE, utilisateur.getRole());
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testLoginSuccess() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("test@example.com");
        utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode("123456"));
        utilisateur.setRole(Role.ROLE_EMPLOYE); // ajoute le rôle ici

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setMotDePasse("123456");

        when(utilisateurRepository.findByEmail("test@example.com")).thenReturn(Optional.of(utilisateur));
        when(jwtService.genrateToken("test@example.com")).thenReturn("mocked-jwt");

        Map<String, Object> result = authService.login(request);

        assertEquals("mocked-jwt", result.get("token"));
        assertEquals("EMPLOYE", result.get("role"));
        verify(jwtService, times(1)).genrateToken("test@example.com");
    }

    @Test
    void testLoginUserNotFound() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("notfound@example.com");
        request.setMotDePasse("password");

        when(utilisateurRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    @Test
    void testLoginIncorrectPassword() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("test@example.com");
        utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode("correct-password"));

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setMotDePasse("wrong-password");

        when(utilisateurRepository.findByEmail("test@example.com")).thenReturn(Optional.of(utilisateur));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("Mot de passe incorrect", exception.getMessage());
    }
}