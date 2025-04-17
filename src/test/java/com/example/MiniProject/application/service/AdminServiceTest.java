package com.example.MiniProject.application.service;

import com.example.MiniProject.domain.model.*;
import com.example.MiniProject.infrastructure.repository.DemandeCongeRepository;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DemandeCongeRepository demandeCongeRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void testGetAllUsers() {
        List<Utilisateur> mockUsers = List.of(new Utilisateur(), new Utilisateur());
        when(utilisateurRepository.findAll()).thenReturn(mockUsers);

        List<Utilisateur> result = adminService.getAllUsers();

        assertEquals(2, result.size());
        verify(utilisateurRepository).findAll();
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        adminService.deleteUser(userId);

        verify(utilisateurRepository).deleteById(userId);
    }

    @Test
    void testUpdateUserRole() {
        Long userId = 1L;
        Utilisateur user = new Utilisateur();
        user.setRole(Role.EMPLOYE);
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(user));

        adminService.updateUserRole(userId, Role.ADMIN);

        assertEquals(Role.ADMIN, user.getRole());
        verify(utilisateurRepository).save(user);
    }

    @Test
    void testCreateUser() {
        Utilisateur user = new Utilisateur();
        user.setMotDePasse("plainPassword");
        Utilisateur savedUser = new Utilisateur();
        savedUser.setMotDePasse("encodedPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(savedUser);

        Utilisateur result = adminService.createUser(user);

        assertEquals("encodedPassword", result.getMotDePasse());
        verify(passwordEncoder).encode("plainPassword");
        verify(utilisateurRepository).save(user);
    }

    @Test
    void testGetCongesParService() {
        Utilisateur u1 = new Utilisateur();
        u1.setService("RH");

        Utilisateur u2 = new Utilisateur();
        u2.setService("IT");

        DemandeConge c1 = new DemandeConge();
        c1.setEmploye(u1);

        DemandeConge c2 = new DemandeConge();
        c2.setEmploye(u2);

        DemandeConge c3 = new DemandeConge();
        c3.setEmploye(u1);

        List<DemandeConge> conges = List.of(c1, c2, c3);

        when(demandeCongeRepository.findByStatus(StatusDemande.APPROUVE)).thenReturn(conges);

        Map<String, Long> result = adminService.getCongesParService();

        assertEquals(2, result.size());
        assertEquals(2L, result.get("RH"));
        assertEquals(1L, result.get("IT"));
    }

    @Test
    void testGetCongesParPeriode() {
        LocalDate debut = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 12, 31);

        DemandeConge c1 = new DemandeConge();
        c1.setDateDebut(LocalDate.of(2024, 3, 1));
        c1.setDateFin(LocalDate.of(2024, 3, 10));

        DemandeConge c2 = new DemandeConge();
        c2.setDateDebut(LocalDate.of(2023, 12, 1));
        c2.setDateFin(LocalDate.of(2023, 12, 10));

        List<DemandeConge> conges = List.of(c1, c2);

        when(demandeCongeRepository.findByStatus(StatusDemande.APPROUVE)).thenReturn(conges);

        List<DemandeConge> result = adminService.getCongesParPeriode(debut, fin);

        assertEquals(1, result.size());
        assertTrue(result.contains(c1));
        assertFalse(result.contains(c2));
    }
}

