package com.example.sprint1.Entity;

public class AuthResponse {
    private String message;
    private String token;
    private int etat;
    private String username;
    private String email;
    private String role;
    private String redirect;

    // Constructeur par défaut
    public AuthResponse() {
    }

    // Constructeur avec des paramètres
    public AuthResponse(String message, String token, int etat, String username, String email, String role) {
        this.message = message;
        this.token = token;
        this.etat = etat;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Constructeur avec des paramètres, y compris le champ 'redirect'
    public AuthResponse(String message, String token, int etat, String username, String email, String role, String redirect) {
        this.message = message;
        this.token = token;
        this.etat = etat;
        this.username = username;
        this.email = email;
        this.role = role;
        this.redirect = redirect;
    }

    // Autres méthodes si nécessaires...
}

