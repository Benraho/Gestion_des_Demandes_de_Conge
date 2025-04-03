package com.example.MiniProject.domain.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    @Column(unique = true)
    private String email;
    private String motDePasse;
    private int soldeConges = 30;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name="manager_id")
    private Utilisateur manager;

    @OneToMany(mappedBy = "manager")
    private List<Utilisateur> employes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getSoldeConges() {
        return soldeConges;
    }

    public void setSoldeConges(int soldeConges) {
        this.soldeConges = soldeConges;
    }

    public List<Utilisateur> getEmployes() {
        return employes;
    }

    public void setEmployes(List<Utilisateur> employes) {
        this.employes = employes;
    }

    public Utilisateur getManager() {
        return manager;
    }

    public void setManager(Utilisateur manager) {
        this.manager = manager;
    }
}
