package com.example.MiniProject.infrastructure.repository;

import com.example.MiniProject.domain.model.HistoriqueAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriqueActionRepository extends JpaRepository<HistoriqueAction , Long> {
}
