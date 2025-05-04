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

        Utilisateur employe = new Utilisateur();
        employe.setId(1L);

        DemandeConge conge1 = new DemandeConge();
        conge1.setEmploye(employe);

        DemandeConge conge2 = new DemandeConge();
        conge2.setEmploye(employe);

        when(repository.findByEmployeId(1L)).thenReturn(Arrays.asList(conge1, conge2));

        List<DemandeConge> result = repository.findByEmployeId(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(conge1));
        assertTrue(result.contains(conge2));
    }

    @Test
    void testFindByEmployeAndStatus() {
        DemandeCongeRepository repository = mock(DemandeCongeRepository.class);

        StatusDemande status =StatusDemande.APPROUVE;

        Utilisateur employe = new Utilisateur();
        employe.setId(1L);

        DemandeConge conge = new DemandeConge();
        conge.setEmploye(employe);
        conge.setStatus(StatusDemande.APPROUVE);

        when(repository.findByEmployeAndStatus(employe, status)).thenReturn(List.of(conge));

        List<DemandeConge> result = repository.findByEmployeAndStatus(employe, status);

        assertEquals(1, result.size());
        assertEquals(StatusDemande.APPROUVE, result.get(0).getStatus());
    }

    @Test
    void testFindByStatus() {
        DemandeCongeRepository repository = mock(DemandeCongeRepository.class);

        StatusDemande status = StatusDemande.APPROUVE;

        DemandeConge conge = new DemandeConge();
        conge.setStatus(StatusDemande.APPROUVE);

        when(repository.findByStatus(status)).thenReturn(List.of(conge));

        List<DemandeConge> result = repository.findByStatus(status);

        assertEquals(1, result.size());
        assertEquals(StatusDemande.APPROUVE, result.get(0).getStatus());
    }
}