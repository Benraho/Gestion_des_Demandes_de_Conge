package com.example.MiniProject.infrastructure.repository;

import com.example.MiniProject.domain.model.DemandeConge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {
    List<DemandeConge> findByEmployeId(Long employeId);
}
