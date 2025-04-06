package com.example.MiniProject.presentation.controller;


import com.example.MiniProject.application.service.HistoriqueActionService;
import com.example.MiniProject.domain.model.HistoriqueAction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/historique")
@RequiredArgsConstructor
public class HistoriqueActionController {

    private final HistoriqueActionService historiqueActionService;

    public HistoriqueActionController(HistoriqueActionService historiqueActionService) {
        this.historiqueActionService = historiqueActionService;
    }

    // Récupérer tous les historiques d'action
    @GetMapping
    public List<HistoriqueAction> getAllActions() {
        return historiqueActionService.getAll();
    }
}