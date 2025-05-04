package com.example.MiniProject.presentation.controller;


import com.example.MiniProject.application.service.AdminService;
import com.example.MiniProject.domain.model.DemandeConge;
import com.example.MiniProject.domain.model.Role;
import com.example.MiniProject.domain.model.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //crée un utilisateur
    @PostMapping("/user")
    public Utilisateur createUser(@RequestBody Utilisateur user){
        return adminService.createUser(user);
    }

    //lister tous les utilisateurs
    @GetMapping("/users")
    public List<Utilisateur> getAllUsers(){
        return adminService.getAllUsers();
    }

    //Supprimer un utilisateur
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        adminService.deleteUser(id);
        return "utilisateur supprimé";
    }

    //Modifier le role de l'utulisateur
    @PutMapping("/users/{id}")
    public String updateRole(@PathVariable Long id , @RequestBody Role role){
        adminService.updateUserRole(id , role);
        return "Role mis à jour";
    }


    @GetMapping("/conges-par-service")
    public Map<String, Long> getCongesParService() {
        return adminService.getCongesParService();
    }

    @GetMapping("/conges-par-periode")
    public List<DemandeConge> getCongesParPeriode(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE

    ) LocalDate dateDebut , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateFin){
        return adminService.getCongesParPeriode(dateDebut , dateFin);
    }


}
