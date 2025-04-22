package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.dto.DemandeCongeDTO;
import com.example.MiniProject.application.service.DemandeCongeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

    private final DemandeCongeService demandeCongeService;

    public ManagerController(DemandeCongeService demandeCongeService) {
        this.demandeCongeService = demandeCongeService;
    }


    @PutMapping("/refuser/{id}")
    public DemandeCongeDTO refuserDemandeConge(@PathVariable Long id ){

        return demandeCongeService.refuserDemande(id);
    }

    @PutMapping("/approuver/{id}")
    public DemandeCongeDTO approuverDemandeConge(@PathVariable Long id ){

        return demandeCongeService.approuverDemande(id);
    }


    @GetMapping("/conges-approuves/{managerId}")
    public List<DemandeCongeDTO> getCongesApprouves(@PathVariable Long managerId) {
        return demandeCongeService.getCongesApprouvesByManager(managerId);
    }
}