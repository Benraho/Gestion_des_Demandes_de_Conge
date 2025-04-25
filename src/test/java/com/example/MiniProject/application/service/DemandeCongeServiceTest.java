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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockitoAnnotations;

public class DemandeCongeServiceTest {

    @Mock
    private DemandeCongeRepository demandeCongeRepository;

    @Mock
    private DemandeCongeMapper demandeCongeMapper;

    @InjectMocks
    private DemandeCongeService demandeCongeService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private HistoriqueActionService historiqueActionService;

    @Mock
    private EmailService emailService;

    @Mock
    private HistoriqueActionRepository historiqueActionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreeDemande() {
        DemandeCongeDTO demandeCongeDTO = new DemandeCongeDTO();
        demandeCongeDTO.setEmployeId(1L);
        demandeCongeDTO.setDateDebut(LocalDate.now());
        demandeCongeDTO.setDateFin(LocalDate.now().plusDays(2));
        demandeCongeDTO.setRaison("Vacances");

        DemandeConge demande =new DemandeConge();
        demande.setStatus("EN_ATTENTE");

        when(demandeCongeMapper.toEntity(demandeCongeDTO)).thenReturn(demande);
        when(demandeCongeRepository.save(any(DemandeConge.class))).thenReturn(demande);
        when(demandeCongeMapper.toDTO(demande)).thenReturn(demandeCongeDTO);

        DemandeCongeDTO result = demandeCongeService.creeDemande(demandeCongeDTO);

        assertNotNull(result);
        assertEquals("Vacances", result.getRaison());
        verify(demandeCongeRepository, times(1)).save(any(DemandeConge.class));
    }

    @Test
    void testAnnulerDemande() {
        Long demandeId = 1L;

        DemandeConge demande = new DemandeConge();
        demande.setId(demandeId);
        demande.setStatus("EN_ATTENTE");

        DemandeCongeDTO dto = new DemandeCongeDTO();
        dto.setId(demandeId);

        when(demandeCongeRepository.findById(demandeId)).thenReturn(Optional.of(demande));
        when(demandeCongeRepository.save(any(DemandeConge.class))).thenReturn(demande);
        when(demandeCongeMapper.toDTO(demande)).thenReturn(dto);

        DemandeCongeDTO result = demandeCongeService.AnnulerDemande(demandeId);

        assertNotNull(result);
        assertEquals(demandeId, result.getId());
        verify(demandeCongeRepository, times(1)).save(demande);
    }

    @Test
    void testGetDemandesByEmploye() {
        Utilisateur employe = new Utilisateur();
        employe.setId(1L);

        DemandeConge demande = new DemandeConge();
        demande.setId(1L);
        demande.setEmploye(employe);
        demande.setStatus("EN_ATTENTE");

        when(demandeCongeRepository.findByEmployeId(1L)).thenReturn(Collections.singletonList(demande));

        var result = demandeCongeService.getDamandesByEmploye(1L);
        assertEquals(1, result.size());
        verify(demandeCongeRepository, times(1)).findByEmployeId(1L);
    }


    @Test
    void testApprouverDemande() {
        Long demandeId = 1L;
        Long employeId = 2L;

        DemandeConge demande = new DemandeConge();
        demande.setId(demandeId);
        demande.setDateDebut(LocalDate.now());
        demande.setDateFin(LocalDate.now().plusDays(2));

        Utilisateur employe = new Utilisateur();
        employe.setId(employeId);
        employe.setSoldeConges(10);
        employe.setEmail("test@email.com");
        demande.setEmploye(employe);

        DemandeCongeDTO dto = new DemandeCongeDTO();
        dto.setId(demandeId);

        when(demandeCongeRepository.findById(demandeId)).thenReturn(Optional.of(demande));
        when(demandeCongeRepository.save(any(DemandeConge.class))).thenReturn(demande);
        when(utilisateurRepository.findById(employeId)).thenReturn(Optional.of(employe));
        when(demandeCongeMapper.toDTO(demande)).thenReturn(dto);

        DemandeCongeDTO result = demandeCongeService.approuverDemande(demandeId);

        assertNotNull(result);
        assertEquals(demandeId, result.getId());
        verify(demandeCongeRepository, times(1)).save(demande);
        verify(utilisateurRepository, times(1)).save(employe);
        verify(historiqueActionService, times(1)).enregistrerHistorique(demandeId, "APPROUVE", 1L);
        verify(emailService, times(1)).envoyerNotification("test@email.com",
                "Demande de congé approuvée", "Votre demande de congé a été approuvée");
    }

    @Test
    void testRefuserDemande() {
        Long demandeId = 1L;
        Long employeId = 2L;
        String email = "employe@example.com";

        DemandeConge demande = new DemandeConge();
        demande.setId(demandeId);

        Utilisateur employe = new Utilisateur();
        employe.setId(employeId);
        employe.setEmail(email);
        demande.setEmploye(employe);

        DemandeCongeDTO dto = new DemandeCongeDTO();

        // Simuler les comportements
        when(demandeCongeRepository.findById(demandeId)).thenReturn(java.util.Optional.of(demande));
        when(utilisateurRepository.findById(employeId)).thenReturn(java.util.Optional.of(employe));
        when(demandeCongeRepository.save(any(DemandeConge.class))).thenReturn(demande);
        when(demandeCongeMapper.toDTO(demande)).thenReturn(dto);

        DemandeCongeDTO result = demandeCongeService.refuserDemande(demandeId);

        assertNotNull(result);
        verify(historiqueActionRepository, times(1)).save(any(HistoriqueAction.class));
        verify(emailService, times(1)).envoyerNotification(eq(email), contains("refusée"), anyString());
    }

    @Test
    void testGetDemandesByManager() {
        Long managerId = 1L;

        // Simule deux employés sous le même manager
        Utilisateur employe1 = new Utilisateur();
        employe1.setId(10L);
        Utilisateur employe2 = new Utilisateur();
        employe2.setId(20L);

        // Simule deux demandes de congés
        DemandeConge conge1 = new DemandeConge();
        conge1.setId(100L);
        conge1.setEmploye(employe1);


        DemandeConge conge2 = new DemandeConge();
        conge2.setId(200L);
        conge2.setEmploye(employe2);

        // Retourne les employés du manager
        when(utilisateurRepository.findByManagerId(managerId)).thenReturn(List.of(employe1, employe2));

        // Retourne toutes les demandes
        when(demandeCongeRepository.findAll()).thenReturn(List.of(conge1, conge2));

        // Mapper les demandes
        when(demandeCongeMapper.toDTO(any(DemandeConge.class)))
                .thenAnswer(invocation -> {
                    DemandeConge d = invocation.getArgument(0);
                    DemandeCongeDTO dto = new DemandeCongeDTO();
                    dto.setId(d.getId());
                    return dto;
                });

        var result = demandeCongeService.getDemandesByManager(managerId);

        assertEquals(2, result.size());
        verify(utilisateurRepository, times(1)).findByManagerId(managerId);
        verify(demandeCongeRepository, times(1)).findAll();
    }
    @Test
    void testGetSoldeConges() {
        Long employeId = 1L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(employeId);
        utilisateur.setSoldeConges(12); // solde simulé

        when(utilisateurRepository.findById(employeId)).thenReturn(java.util.Optional.of(utilisateur));

        int solde = demandeCongeService.getSoldeConges(employeId);

        assertEquals(12, solde);
        verify(utilisateurRepository, times(1)).findById(employeId);
    }

    @Test
    void testGetCongesApprouvesByManager() {
        Long managerId = 1L;

        // Simuler un employé sous ce manager
        Utilisateur employe = new Utilisateur();
        Utilisateur manager = new Utilisateur();
        manager.setId(managerId);
        employe.setId(2L);
        employe.setManager(manager);

        // Simuler une demande de congé approuvée
        DemandeConge demande = new DemandeConge();
        demande.setId(10L);
        demande.setEmploye(employe);
        demande.setStatus(StatusDemande.APPROUVE.name());

        // DTO attendu
        DemandeCongeDTO dto = new DemandeCongeDTO();
        dto.setId(10L);

        // Mock des dépendances
        when(utilisateurRepository.findByManagerId(managerId)).thenReturn(List.of(employe));
        when(demandeCongeRepository.findByEmployeAndStatus(employe, StatusDemande.APPROUVE)).thenReturn(List.of(demande));
        when(demandeCongeMapper.toDTO(demande)).thenReturn(dto);

        List<DemandeCongeDTO> result = demandeCongeService.getCongesApprouvesByManager(managerId);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        verify(utilisateurRepository, times(1)).findByManagerId(managerId);
        verify(demandeCongeRepository, times(1)).findByEmployeAndStatus(employe, StatusDemande.APPROUVE);
    }
}