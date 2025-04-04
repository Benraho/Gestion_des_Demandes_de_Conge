package com.example.MiniProject.presentation.controller;


import com.example.MiniProject.application.service.UtilisateurService;
import com.example.MiniProject.domain.model.Utilisateur;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/manager/{id}")
    public List<Utilisateur> getEmployesByManager(@PathVariable Long id){
        return utilisateurService.getEmployesByManagerId(id);
    }

    @GetMapping("/{id}")
    public Utilisateur getUtilisateur(@PathVariable Long id){
        return utilisateurService.getUtilisateurById(id);
    }
}
