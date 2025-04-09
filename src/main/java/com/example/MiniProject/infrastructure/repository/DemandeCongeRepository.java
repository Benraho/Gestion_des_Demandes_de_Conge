package com.example.MiniProject.infrastructure.repository;

import com.example.MiniProject.domain.model.DemandeConge;
import com.example.MiniProject.domain.model.StatusDemande;
import com.example.MiniProject.domain.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {
    List<DemandeConge> findByEmployeId(Long employeId);
    List<DemandeConge> findByEmployeAndStatut(Utilisateur employe, StatusDemande statut);

    @Query("SELECT d FROM DemandeConge d WHERE d.status = 'APPROUVE'")
    List<DemandeConge> findByStatus(StatusDemande statusDemande);
}
