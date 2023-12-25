package com.example.sprint1.Entity;

public class SignUpRequest {
    private String username;
    private String password;
    private String email;
    private int numInscrit;
    private String userClasse;

    // Ajoutez les getters et setters appropriés

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

    public int getNumInscrit() {
        return numInscrit;
    }

    public void setNumInscrit(int numInscrit) {
        this.numInscrit = numInscrit;
    }

    public String getUserClasse() {
        return userClasse;
    }

    public void setUserClasse(String userClasse) {
        this.userClasse = userClasse;
    }
}

