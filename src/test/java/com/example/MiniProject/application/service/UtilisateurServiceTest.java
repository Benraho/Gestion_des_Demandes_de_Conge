package com.example.MiniProject.application.service;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

        import java.util.List;
import java.util.Optional;

import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Test
    void testGetEmployesByManagerId() {
        Long managerId = 1L;
        Utilisateur employe1 = new Utilisateur();
        Utilisateur employe2 = new Utilisateur();
        List<Utilisateur> employes = List.of(employe1, employe2);

        when(utilisateurRepository.findByManagerId(managerId)).thenReturn(employes);

        List<Utilisateur> result = utilisateurService.getEmployesByManagerId(managerId);

        assertEquals(2, result.size());
        assertTrue(result.contains(employe1));
        assertTrue(result.contains(employe2));
    }

    @Test
    void testGetUtilisateurById() {
        Long id = 1L;
        Utilisateur utilisateur = new Utilisateur();

        when(utilisateurRepository.findById(id)).thenReturn(Optional.of(utilisateur));

        Utilisateur result = utilisateurService.getUtilisateurById(id);

        assertNotNull(result);
        assertEquals(utilisateur, result);
    }
}
