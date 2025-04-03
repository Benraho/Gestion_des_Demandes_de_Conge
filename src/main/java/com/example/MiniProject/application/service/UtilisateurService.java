package com.example.MiniProject.application.service;

import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import jakarta.validation.constraints.Null;
import org.hibernate.service.spi.InjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Utilisateur> getEmployesByManagerId(Long managerId){
        return utilisateurRepository.findByManagerId(managerId);
    }

    public Utilisateur getUtilisateurById(Long id){
        return utilisateurRepository.findById(id).orElse(null);
    }
}
