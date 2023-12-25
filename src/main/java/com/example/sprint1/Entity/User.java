package com.example.sprint1.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role = "eleve";
    private int etat = 0;
    private String userClasse;
    private int numInscrit;
    public int getNumInscrit() {
        return numInscrit;
    }

    public void setNumInscrit(int numInscrit) {
        this.numInscrit = numInscrit;
    }

     // Ajoutez cette ligne pour le champ numInscrit

    // Les getters et setters appropriés



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getUserClasse() {
        return userClasse;
    }

    public void setUserClasse(String userClasse) {
        this.userClasse = userClasse;
    }
}
