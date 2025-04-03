package com.example.MiniProject.presentation.controller;


import com.example.MiniProject.application.service.AdminService;
import com.example.MiniProject.domain.model.Role;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //cree un utilisateur
    @PostMapping("/users")
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
}
