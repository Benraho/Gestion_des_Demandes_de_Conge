package com.example.MiniProject.application.service;


import com.example.MiniProject.domain.model.HistoriqueAction;
import com.example.MiniProject.infrastructure.repository.HistoriqueActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoriqueActionService {

    private final HistoriqueActionRepository historiqueActionRepository;

    public HistoriqueActionService(HistoriqueActionRepository historiqueActionRepository) {
        this.historiqueActionRepository = historiqueActionRepository;
    }

    public void enregistrerHistorique(Long demandeId, String action, Long managerId) {
        HistoriqueAction historique = new HistoriqueAction();
        historique.setDemandeId(demandeId);
        historique.setAction(action);
        historique.setManagerId(managerId);
        historique.setDateAction(java.time.LocalDateTime.now());
        historiqueActionRepository.save(historique);
    }

    public List<HistoriqueAction> getAll() {
        return historiqueActionRepository.findAll();
    }
}