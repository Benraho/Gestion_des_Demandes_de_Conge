package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.dto.LoginRequestDTO;
import com.example.MiniProject.application.service.AuthService;
import com.example.MiniProject.domain.model.Utilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("samira@test.com");
        loginRequest.setMotDePasse("password");


        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("token", "mock-jwt-token");
        mockResponse.put("role", "ROLE_ADMIN");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    void testLoginFailureWrongPassword() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("samira@test.com");
        loginRequest.setMotDePasse("wrong-password");

        when(authService.login(any(LoginRequestDTO.class))).thenThrow(new RuntimeException("Mot de passe incorrect"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("notfound@test.com");
        loginRequest.setMotDePasse("any");

        when(authService.login(any(LoginRequestDTO.class))).thenThrow(new RuntimeException("Utilisateur non trouvé"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRegister() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("Samira");
        utilisateur.setEmail("samira@test.com");
        utilisateur.setMotDePasse("password");

        when(authService.register(any(Utilisateur.class))).thenReturn("Inscription réussite");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilisateur)))
                .andExpect(status().isOk())
                .andExpect(content().string("Inscription réussite"));
    }
}