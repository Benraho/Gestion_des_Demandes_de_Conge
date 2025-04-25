package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.dto.DemandeCongeDTO;
import com.example.MiniProject.application.service.DemandeCongeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagerController.class)
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DemandeCongeService demandeCongeService;

    @Test
    @WithMockUser(roles = "MANAGER")
    void testRefuserDemande() throws Exception {
        when(demandeCongeService.refuserDemande(1L)).thenReturn(new DemandeCongeDTO());

        mockMvc.perform(put("/api/manager/refuser/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void testApprouverDemande() throws Exception {
        when(demandeCongeService.approuverDemande(1L)).thenReturn(new DemandeCongeDTO());

        mockMvc.perform(put("/api/manager/approuver/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void testGetCongesApprouves() throws Exception {
        when(demandeCongeService.getCongesApprouvesByManager(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/manager/conges-approuves/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}