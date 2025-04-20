package com.example.MiniProject.config;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        EmployeTestController.class,
        ManagerTestController.class,
        AdminTestController.class,
        AuthTestController.class
})
@Import(SecurityConfig.class) // on importe la config de sécurité
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/api/auth/login")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "EMPLOYE")
    void testEmployeAccess_allowed() throws Exception {
        mockMvc.perform(get("/api/conges/employe/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void testEmployeAccess_forbiddenForManager() throws Exception {
        mockMvc.perform(get("/api/conges/employe/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void testManagerAccess_allowed() throws Exception {
        mockMvc.perform(post("/api/conges/approuver/1")).andExpect(status().isOk());
        mockMvc.perform(post("/api/conges/refuser/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "EMPLOYE")
    void testManagerAccess_forbiddenForEmploye() throws Exception {
        mockMvc.perform(post("/api/conges/approuver/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminAccess_allowed() throws Exception {
        mockMvc.perform(get("/api/admin")).andExpect(status().isOk());
    }

    @Test
    void testUnauthorizedAccess_allForbidden() throws Exception {
        mockMvc.perform(get("/api/conges/employe/1")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/conges/approuver/1")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/conges/refuser/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/admin")).andExpect(status().isForbidden());
    }
}