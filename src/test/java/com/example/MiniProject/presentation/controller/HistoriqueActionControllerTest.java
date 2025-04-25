package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.service.HistoriqueActionService;
import com.example.MiniProject.domain.model.HistoriqueAction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoriqueActionController.class)
class HistoriqueActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoriqueActionService historiqueActionService;

    @Test
    @WithMockUser(roles = "MANAGER")
    void testGetAllActions() throws Exception {
        HistoriqueAction a1 = new HistoriqueAction();
        a1.setId(1L);
        HistoriqueAction a2 = new HistoriqueAction();
        a2.setId(2L);

        List<HistoriqueAction> actions = Arrays.asList(a1 ,a2);

        when(historiqueActionService.getAll()).thenReturn(actions);

        mockMvc.perform(get("/api/historique").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }
}