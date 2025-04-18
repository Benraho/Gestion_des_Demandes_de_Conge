package com.example.MiniProject.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.example.MiniProject.domain.model.DemandeConge;
import com.example.MiniProject.domain.model.StatusDemande;
import com.example.MiniProject.domain.model.Utilisateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DemandeCongeRepositoryTest {

    @Autowired
    private DemandeCongeRepository demandeCongeRepository;

    @Test
    void testFindByEmployeId() {
        Long employeId = 1L;

        DemandeConge conge1 = new DemandeConge();
        conge1.setEmployeId(employeId);
        conge1.setStatus("APPROUVE");
        demandeCongeRepository.save(conge1);

        DemandeConge conge2 = new DemandeConge();
        conge2.setEmployeId(employeId);
        conge2.setStatus("REJETE");
        demandeCongeRepository.save(conge2);

        List<DemandeConge> conges = demandeCongeRepository.findByEmployeId(employeId);

        assertNotNull(conges);
        assertEquals(2, conges.size());
        assertTrue(conges.contains(conge1));
        assertTrue(conges.contains(conge2));
    }

    @Test
    void testFindByEmployeAndStatut() {
        Utilisateur employe = new Utilisateur();
        employe.setId(1L);
        String statut = "APPROUVE";

        DemandeConge conge1 = new DemandeConge();
        conge1.setEmploye(employe);
        conge1.setStatus(statut);
        demandeCongeRepository.save(conge1);

        DemandeConge conge2 = new DemandeConge();
        conge2.setEmploye(employe);
        conge2.setStatus("REJETE");
        demandeCongeRepository.save(conge2);

        List<DemandeConge> conges = demandeCongeRepository.findByEmployeAndStatut(employe, StatusDemande.valueOf(statut));

        assertNotNull(conges);
        assertEquals(1, conges.size());
        assertTrue(conges.contains(conge1));
        assertFalse(conges.contains(conge2));
    }

    @Test
    void testFindByStatus() {
        String statut = "APPROUVE";

        // Préparation des données de test
        DemandeConge conge1 = new DemandeConge();
        conge1.setStatus(statut);
        demandeCongeRepository.save(conge1);

        DemandeConge conge2 = new DemandeConge();
        conge2.setStatus("REJETE");
        demandeCongeRepository.save(conge2);

        List<DemandeConge> conges = demandeCongeRepository.findByStatus(StatusDemande.valueOf(statut));

        assertNotNull(conges);
        assertEquals(1, conges.size());
        assertTrue(conges.contains(conge1));
        assertFalse(conges.contains(conge2));
    }
}

