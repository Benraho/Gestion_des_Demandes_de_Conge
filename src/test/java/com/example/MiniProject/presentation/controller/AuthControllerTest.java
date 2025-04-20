package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import com.example.MiniProject.infrastructure.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(com.example.MiniProject.config.SecurityConfig.class)
@WithMockUser
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void testRegister() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("John Doe");
        utilisateur.setEmail("john@example.com");
        utilisateur.setMotDePasse("password");

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(utilisateur)))
                .andExpect(status().isOk())
                .andExpect(content().string("Inscription réussite"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("john@example.com");
        utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode("password"));

        when(utilisateurRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(utilisateur));
        when(jwtService.genrateToken("john@example.com"))
                .thenReturn("fake-jwt-token");

        String requestBody = """
                {
                  "email": "john@example.com",
                  "motDePasse": "password"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("fake-jwt-token"));
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("john@example.com");
        utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode("password"));

        when(utilisateurRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(utilisateur));

        String requestBody = """
                {
                  "email": "john@example.com",
                  "motDePasse": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        when(utilisateurRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        String requestBody = """
                {
                  "email": "unknown@example.com",
                  "motDePasse": "any"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().is4xxClientError());
    }
}