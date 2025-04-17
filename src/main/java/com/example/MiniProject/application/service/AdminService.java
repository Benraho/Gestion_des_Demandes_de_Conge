package com.example.MiniProject.application.service;

import com.example.MiniProject.domain.model.DemandeConge;
import com.example.MiniProject.domain.model.Role;
import com.example.MiniProject.domain.model.StatusDemande;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.DemandeCongeRepository;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final DemandeCongeRepository demandeCongeRepository;

    public AdminService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, DemandeCongeRepository demandeCongeRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.demandeCongeRepository = demandeCongeRepository;
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



    public Map<String, Long> getCongesParService() {
         List<DemandeConge> conges = demandeCongeRepository.findByStatus(StatusDemande.APPROUVE);
        return conges.stream()
                .collect(Collectors.groupingBy(c -> c.getEmploye().getService(), Collectors.counting()));
    }


    public List<DemandeConge> getCongesParPeriode(LocalDate debut, LocalDate fin) {
        List<DemandeConge> conges = demandeCongeRepository.findByStatus(StatusDemande.APPROUVE);
        return conges.stream().filter(c -> !c.getDateDebut().isBefore(debut) && !c.getDateFin().isAfter(fin))
                .collect(Collectors.toList());
    }
}
