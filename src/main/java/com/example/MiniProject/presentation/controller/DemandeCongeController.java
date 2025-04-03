package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.dto.DemandeCongeDTO;
import com.example.MiniProject.application.service.DemandeCongeService;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conges")
@RequiredArgsConstructor
public class DemandeCongeController {
    private final DemandeCongeService demandeCongeService;
    private final UtilisateurRepository utilisateurRepository;

    public DemandeCongeController(DemandeCongeService demandeCongeService, UtilisateurRepository utilisateurRepository) {
        this.demandeCongeService = demandeCongeService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @PostMapping
    public DemandeCongeDTO creeDemande(@RequestBody DemandeCongeDTO dto){
        return demandeCongeService.creeDemande(dto);
    }

    @GetMapping("/employe/{id} ")
    public List<DemandeCongeDTO> getDemandesEmploye(@PathVariable Long id){
        return demandeCongeService.getDamandesByEmploye(id);
    }

    @PutMapping("/approuver/{id}")
    public DemandeCongeDTO refuser(@PathVariable Long id ){
        return demandeCongeService.refuserDemande(id);
    }

    @GetMapping("/solde/{id")
    public int getSoldeConges(@PathVariable Long id){
        return utilisateurRepository.findById(id).map(Utilisateur::getSoldeConges).orElse(0);
    }
}
