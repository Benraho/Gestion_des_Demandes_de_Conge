package com.example.MiniProject.application.dto;

import com.example.MiniProject.domain.model.StatusDemande;

import java.time.LocalDate;

public class DemandeCongeDTO {
    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatusDemande statut;
    private Long employeId;
    private String raison;

    public Long getEmployeId() {
        return employeId;
    }

    public void setEmployeId(Long employeId) {
        this.employeId = employeId;
    }

    public StatusDemande getStatut() {
        return statut;
    }

    public void setStatut(StatusDemande statut) {
        this.statut = statut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }
}
