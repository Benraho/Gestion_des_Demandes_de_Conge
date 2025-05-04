package com.example.MiniProject.application.service;


import com.example.MiniProject.domain.model.HistoriqueAction;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.HistoriqueActionRepository;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoriqueActionService {

    private final HistoriqueActionRepository historiqueActionRepository;
    private final UtilisateurRepository utilisateurRepository;

    public HistoriqueActionService(HistoriqueActionRepository historiqueActionRepository, UtilisateurRepository utilisateurRepository) {
        this.historiqueActionRepository = historiqueActionRepository;
        this.utilisateurRepository = utilisateurRepository;
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

    public List<HistoriqueAction> getHistoriquePourUtilisateur() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Si c'est un EMPLOYE :voir ses propres demandes
        if (utilisateur.getRole().name().equals("ROLE_EMPLOYE")) {
            return historiqueActionRepository.findByDemandeEmployeId(utilisateur.getId());
        }

        // Si c'est un MANAGER :voir les demandes de ses employés
        if (utilisateur.getRole().name().equals("ROLE_MANAGER")) {
            List<Long> employeIds = utilisateur.getEmployes().stream()
                    .map(Utilisateur::getId)
                    .toList();
            return historiqueActionRepository.findByDemandeEmployeIds(employeIds);
        }

        // Si c'est un ADMIN : voir tout
        return historiqueActionRepository.findAll();
    }
}