package com.example.MiniProject.application.service;


import com.example.MiniProject.application.dto.DemandeCongeDTO;
import com.example.MiniProject.application.mapper.DemandeCongeMapper;
import com.example.MiniProject.domain.model.DemandeConge;
import com.example.MiniProject.domain.model.HistoriqueAction;
import com.example.MiniProject.domain.model.StatusDemande;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.DemandeCongeRepository;
import com.example.MiniProject.infrastructure.repository.HistoriqueActionRepository;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import com.example.MiniProject.infrastructure.security.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DemandeCongeService {
    private final DemandeCongeRepository demandeCongeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final HistoriqueActionRepository historiqueActionRepository;
    private final EmailService emailService;
    private final HistoriqueActionService historiqueActionService;
    private final DemandeCongeMapper demandeCongeMapper;


    public DemandeCongeService(DemandeCongeRepository demandeCongeRepository, DemandeCongeMapper mapper, UtilisateurRepository utilisateurRepository, HistoriqueActionRepository historiqueActionRepository, EmailService emailService, HistoriqueActionService historiqueActionService, DemandeCongeMapper demandeCongeMapper) {
        this.demandeCongeRepository = demandeCongeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.historiqueActionRepository = historiqueActionRepository;
        this.emailService = emailService;
        this.historiqueActionService = historiqueActionService;
        this.demandeCongeMapper = demandeCongeMapper;
    }

    public DemandeCongeDTO creeDemande(DemandeCongeDTO dto){
        DemandeConge entity = demandeCongeMapper.toEntity(dto);
        entity.setStatus("EN_ATTENTE");
        return demandeCongeMapper.toDTO(demandeCongeRepository.save(entity));
    }

    public DemandeCongeDTO AnnulerDemande(Long id){
        DemandeConge demande = demandeCongeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Demande non trouvée"));
        if(!demande.getStatus().equals("EN_ATTENTE")) {
            throw new RuntimeException("Impossible d'annuler une demande déja traitée");
        }
        demande.setStatus(String.valueOf(StatusDemande.ANNULEE));
        demandeCongeRepository.save(demande);
        return demandeCongeMapper.toDTO(demande);
    }

    public List<DemandeCongeDTO> getDamandesByEmploye(Long employeId){
        return demandeCongeRepository.findByEmployeId(employeId).stream()
                .map(demandeCongeMapper::toDTO)
                .collect(Collectors.toList());

    }

    public DemandeCongeDTO approuverDemande(Long id){
        DemandeConge demande =demandeCongeRepository.findById(id).orElseThrow();
        demande.setStatus("APPROUVE");
        DemandeConge saved = demandeCongeRepository.save(demande);


        //mise a jour du solde congé
        Utilisateur user = utilisateurRepository.findById(demande.getEmployeId()).orElseThrow();
        long nbJours = ChronoUnit.DAYS.between(demande.getDateDebut(), demande.getDateFin()) + 1;
        user.setSoldeConges(user.getSoldeConges() -(int) nbJours);
        utilisateurRepository.save(user);

        //Historique
        historiqueActionService.enregistrerHistorique(demande.getId(), "APPROUVE", 1L);

        //Notification
        emailService.envoyerNotification(user.getEmail(),
                "Demande de congé approuvée",
                "Votre demande de congé a été approuvée");

        return  demandeCongeMapper.toDTO(saved);
    }

    public DemandeCongeDTO refuserDemande(Long id){
        DemandeConge demande =demandeCongeRepository.findById(id).orElseThrow();
        demande.setStatus("REFUSE");
        DemandeConge saved = demandeCongeRepository.save(demande);

        //historique
        HistoriqueAction historiqueAction = new HistoriqueAction();
        historiqueAction.setDemandeId(demande.getId());
        historiqueAction.setManagerId(1L);
        historiqueAction.setAction("REFUSE");
        historiqueActionRepository.save(historiqueAction);


        //Notification
        Utilisateur user = utilisateurRepository.findById(demande.getEmployeId()).orElseThrow();
        emailService.envoyerNotification(user.getEmail() , "demande de congé refusée" , " Votre demande de congée a été refusée");
        return demandeCongeMapper.toDTO(saved);
    }

    public List<DemandeCongeDTO> getDemandesByManager(Long managerId){
        List<Utilisateur> employes =utilisateurRepository.findByManagerId(managerId);
        List<Long> employeIds =employes.stream().map(Utilisateur::getId).toList();

        return demandeCongeRepository.findAll().stream().filter(d -> employeIds.contains(d.getEmployeId())).map(demandeCongeMapper::toDTO).collect(Collectors.toList());
    }

    public int getSoldeConges(Long id){
        return utilisateurRepository.findById(id)
                .map(Utilisateur::getSoldeConges)
                .orElse(0);
    }

    public List<DemandeCongeDTO> getCongesApprouvesByManager(Long managerId) {
        List<Utilisateur> employes = utilisateurRepository.findByManagerId(managerId);

        return employes.stream()
                .flatMap(employe -> demandeCongeRepository.findByEmployeAndStatut(employe, StatusDemande.APPROUVE).stream())
                .map(demandeCongeMapper::toDTO)
                .collect(Collectors.toList());
    }

}
