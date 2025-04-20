package com.example.MiniProject.infrastructure.repository;

import com.example.MiniProject.domain.model.DemandeConge;
import com.example.MiniProject.domain.model.StatusDemande;
import com.example.MiniProject.domain.model.Utilisateur;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DemandeCongeRepositoryTest {

    @Test
    void testFindByEmployeId() {
        DemandeCongeRepository repository = mock(DemandeCongeRepository.class);

        Long employeId = 1L;
        DemandeConge conge1 = new DemandeConge();
        conge1.setEmployeId(employeId);

        DemandeConge conge2 = new DemandeConge();
        conge2.setEmployeId(employeId);

        when(repository.findByEmployeId(employeId)).thenReturn(Arrays.asList(conge1, conge2));

        List<DemandeConge> result = repository.findByEmployeId(employeId);

        assertEquals(2, result.size());
        assertTrue(result.contains(conge1));
        assertTrue(result.contains(conge2));
    }

    @Test
    void testFindByEmployeAndStatut() {
        DemandeCongeRepository repository = mock(DemandeCongeRepository.class);

        Utilisateur employe = new Utilisateur();
        StatusDemande statut = StatusDemande.APPROUVE;

        DemandeConge conge = new DemandeConge();
        conge.setEmploye(employe);
        conge.setStatus("APPROUVE");

        when(repository.findByEmployeAndStatut(employe, statut)).thenReturn(List.of(conge));

        List<DemandeConge> result = repository.findByEmployeAndStatut(employe, statut);

        assertEquals(1, result.size());
        assertEquals("APPROUVE", result.get(0).getStatus());
    }

    @Test
    void testFindByStatus() {
        DemandeCongeRepository repository = mock(DemandeCongeRepository.class);

        StatusDemande statut = StatusDemande.APPROUVE;
        DemandeConge conge = new DemandeConge();
        conge.setStatus("APPROUVE");

        when(repository.findByStatus(statut)).thenReturn(List.of(conge));

        List<DemandeConge> result = repository.findByStatus(statut);

        assertEquals(1, result.size());
        assertEquals("APPROUVE", result.get(0).getStatus());
    }
}