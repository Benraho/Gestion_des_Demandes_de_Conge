package com.example.MiniProject.infrastructure.repository;

import com.example.MiniProject.domain.model.HistoriqueAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoriqueActionRepository extends JpaRepository<HistoriqueAction , Long> {
    @Query("SELECT h FROM HistoriqueAction h WHERE h.demandeId IN (SELECT d.id FROM DemandeConge d WHERE d.employe.id = :employeId)")
    List<HistoriqueAction> findByDemandeEmployeId(@Param("employeId") Long employeId);

    @Query("SELECT h FROM HistoriqueAction h WHERE h.demandeId IN (SELECT d.id FROM DemandeConge d WHERE d.employe.id IN :employeIds)")
    List<HistoriqueAction> findByDemandeEmployeIds(@Param("employeIds") List<Long> employeIds);
}
