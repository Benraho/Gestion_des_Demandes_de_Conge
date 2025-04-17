package com.example.MiniProject.application.service;

import com.example.MiniProject.application.dto.DemandeCongeDTO;
import com.example.MiniProject.application.mapper.DemandeCongeMapper;
import com.example.MiniProject.domain.model.DemandeConge;
import com.example.MiniProject.domain.model.Utilisateur;
import com.example.MiniProject.infrastructure.repository.DemandeCongeRepository;
import com.example.MiniProject.infrastructure.repository.UtilisateurRepository;
import com.example.MiniProject.infrastructure.security.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
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
    void testApprouverDemande() {
        Long demandeId = 1L;
        Long employeId = 2L;

        DemandeConge demande = new DemandeConge();
        demande.setId(demandeId);
        demande.setEmployeId(employeId);
        demande.setDateDebut(LocalDate.now());
        demande.setDateFin(LocalDate.now().plusDays(2));

        Utilisateur employe = new Utilisateur();
        employe.setId(employeId);
        employe.setSoldeConges(10);
        employe.setEmail("test@email.com");

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
}