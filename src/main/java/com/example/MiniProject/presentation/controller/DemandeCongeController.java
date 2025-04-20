package com.example.MiniProject.presentation.controller;

import com.example.MiniProject.application.dto.DemandeCongeDTO;
import com.example.MiniProject.application.service.DemandeCongeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conges")
//@RequiredArgsConstructor
public class DemandeCongeController {
    private final DemandeCongeService demandeCongeService;

    public DemandeCongeController(DemandeCongeService demandeCongeService) {
        this.demandeCongeService = demandeCongeService;
    }

    @PostMapping
    public DemandeCongeDTO creeDemande(@RequestBody DemandeCongeDTO dto){
        return demandeCongeService.creeDemande(dto);
    }

    @PutMapping("/annuler/{id}")
    public DemandeCongeDTO annulerDemandeConge(@PathVariable Long id){
        return demandeCongeService.AnnulerDemande(id);
    }

    @GetMapping("/employe/{id}")
    public List<DemandeCongeDTO> getDemandesEmploye(@PathVariable Long id){
        return demandeCongeService.getDamandesByEmploye(id);
    }

    @GetMapping("/manager/{id}")
    public List<DemandeCongeDTO> getDemandesByManager(@PathVariable Long id){
        return demandeCongeService.getDemandesByManager(id);
    }

    @GetMapping("/solde/{id}")
    public int getSoldeConges(@PathVariable Long id){
        return demandeCongeService.getSoldeConges(id);
    }

}
