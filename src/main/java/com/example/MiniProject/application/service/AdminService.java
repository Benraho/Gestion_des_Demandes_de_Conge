package com.example.MiniProject.application.service;

import com.example.MiniProject.domain.model.Role;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> getAllUsers(){
        return utilisateurRepository.findAll();
    }

    public void deleteUser(Long id){
        utilisateurRepository.deleteById(id);
    }

    public void updateUserRole(Long id , Role role){
        Utilisateur user = utilisateurRepository.findById(id).orElseThrow();
        user.setRole(role);
        utilisateurRepository.save(user);
    }

    public Utilisateur createUser(Utilisateur user){
        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
        return utilisateurRepository.save(user);
    }
}
