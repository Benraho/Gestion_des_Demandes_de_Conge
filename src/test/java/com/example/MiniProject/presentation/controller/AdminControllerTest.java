package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.service.AdminService;
import com.example.MiniProject.config.SecurityConfig;
import com.example.MiniProject.domain.model.DemandeConge;
import com.example.MiniProject.domain.model.Role;
import com.example.MiniProject.domain.model.Utilisateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@WithMockUser(roles = "ADMIN")
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    void testCreateUser() throws Exception {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setNom("John Doe");

        when(adminService.createUser(any(Utilisateur.class))).thenReturn(user);

        mockMvc.perform(post("/api/admin/users")
                        .contentType("application/json")
                        .content("{\"nom\":\"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nom").value("John Doe"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        Utilisateur user1 = new Utilisateur();
        user1.setId(1L);
        user1.setNom("John Doe");

        Utilisateur user2 = new Utilisateur();
        user2.setId(2L);
        user2.setNom("Jane Doe");

        when(adminService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;
        doNothing().when(adminService).deleteUser(userId);

        mockMvc.perform(delete("/api/admin/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("utilisateur supprimé"));
    }

    @Test
    void testUpdateRole() throws Exception {
        Long userId = 1L;
        doNothing().when(adminService).updateUserRole(userId, Role.ROLE_ADMIN);

        mockMvc.perform(put("/api/admin/users/{id}", userId)
                        .contentType("application/json")
                        .content("\"ROLE_ADMIN\""))
                .andExpect(status().isOk())
                .andExpect(content().string("Role mis à jour"));
    }

    @Test
    void testGetCongesParService() throws Exception {
        when(adminService.getCongesParService()).thenReturn(Map.of("IT", 5L));

        mockMvc.perform(get("/api/admin/conges-par-service"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.IT").value(5L));
    }

    @Test
    void testGetCongesParPeriode() throws Exception {
        LocalDate dateDebut = LocalDate.of(2024, 1, 1);
        LocalDate dateFin = LocalDate.of(2024, 12, 31);

        DemandeConge c = new DemandeConge();
        c.setDateDebut(LocalDate.of(2024, 3, 1));
        c.setDateFin(LocalDate.of(2024, 3, 10));

        when(adminService.getCongesParPeriode(dateDebut, dateFin)).thenReturn(List.of(c));

        mockMvc.perform(get("/api/admin/conges-par-periode")
                        .param("dateDebut", "2024-01-01")
                        .param("dateFin", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}