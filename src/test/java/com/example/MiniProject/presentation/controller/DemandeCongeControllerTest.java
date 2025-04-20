package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.dto.DemandeCongeDTO;
import com.example.MiniProject.application.service.DemandeCongeService;
import com.example.MiniProject.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DemandeCongeController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class DemandeCongeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DemandeCongeService demandeCongeService;

    @Test
    void testCreeDemande() throws Exception {
        DemandeCongeDTO dto = new DemandeCongeDTO();
        dto.setId(1L);

        when(demandeCongeService.creeDemande(any(DemandeCongeDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/conges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testAnnulerDemande() throws Exception {
        DemandeCongeDTO dto = new DemandeCongeDTO();
        dto.setId(1L);

        when(demandeCongeService.AnnulerDemande(1L)).thenReturn(dto);

        mockMvc.perform(put("/api/conges/annuler/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetDemandesEmploye() throws Exception {
        DemandeCongeDTO dto = new DemandeCongeDTO();
        dto.setId(1L);

        when(demandeCongeService.getDamandesByEmploye(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/conges/employe/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetDemandesManager() throws Exception {
        DemandeCongeDTO dto = new DemandeCongeDTO();
        dto.setId(1L);

        when(demandeCongeService.getDemandesByManager(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/conges/manager/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetSoldeConges() throws Exception {
        when(demandeCongeService.getSoldeConges(1L)).thenReturn(12);

        mockMvc.perform(get("/api/conges/solde/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("12"));
    }
}