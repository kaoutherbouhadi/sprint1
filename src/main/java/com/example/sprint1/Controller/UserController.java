package com.example.sprint1.Controller;

import com.example.sprint1.Dao.UserRepository;
import com.example.sprint1.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.example.sprint1.Entity.AuthResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        try {
            User user = userRepository.findByUsername(signInRequest.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Nom d'utilisateur incorrect."));
            }

            if (user.getEtat() != 1) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("L'utilisateur n'a pas encore été accepté."));
            }

            if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Mot de passe incorrect."));
            }

            String token = generateAuthToken(user);
            if ("eleve".equals(user.getRole())) {
                AuthResponse authResponse = new AuthResponse("Connexion réussie", token, user.getEtat(),
                        user.getUsername(), user.getEmail(), user.getRole(), "/eleve-page");
                return ResponseEntity.ok(authResponse);
            } else {
                AuthResponse authResponse = new AuthResponse("Connexion réussie", token, user.getEtat(),
                        user.getUsername(), user.getEmail(), user.getRole());
                return ResponseEntity.ok(authResponse);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the error to the console for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Une erreur est survenue lors de la connexion: " + e.getMessage()));
        }
    }

    // Other methods...

    private String generateAuthToken(User user) {
        // Generate and return the authentication token
        return Jwts.builder()
                .setSubject(user.getUsername()) // Use the username as the token subject
                .claim("username", user.getUsername()) // Add other claims as needed
                .signWith(SignatureAlgorithm.HS256, "your_secret_key") // Use a secret key to sign the token
                .compact();
    }
}
