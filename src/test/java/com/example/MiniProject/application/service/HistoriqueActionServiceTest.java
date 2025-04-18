package com.example.MiniProject.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import com.example.MiniProject.domain.model.HistoriqueAction;
import com.example.MiniProject.infrastructure.repository.HistoriqueActionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class HistoriqueActionServiceTest {

    @Mock
    private HistoriqueActionRepository historiqueActionRepository;

    @InjectMocks
    private HistoriqueActionService historiqueActionService;

    @Test
    void testEnregistrerHistorique() {
        Long demandeId = 1L;
        String action = "APPROVE";
        Long managerId = 2L;

        HistoriqueAction historique = new HistoriqueAction();
        historique.setDemandeId(demandeId);
        historique.setAction(action);
        historique.setManagerId(managerId);
        historique.setDateAction(java.time.LocalDateTime.now());

        historiqueActionService.enregistrerHistorique(demandeId, action, managerId);

        verify(historiqueActionRepository, times(1)).save(any(HistoriqueAction.class));
    }

    @Test
    void testGetAll() {
        HistoriqueAction historique1 = new HistoriqueAction();
        HistoriqueAction historique2 = new HistoriqueAction();
        List<HistoriqueAction> historiques = List.of(historique1, historique2);

        when(historiqueActionRepository.findAll()).thenReturn(historiques);

        List<HistoriqueAction> result = historiqueActionService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(historique1));
        assertTrue(result.contains(historique2));
    }
}
