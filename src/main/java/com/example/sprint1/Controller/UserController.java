package com.example.sprint1.Controller;

import com.example.sprint1.Dao.UserRepository;
import com.example.sprint1.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.example.sprint1.Entity.AuthResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

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
                return ResponseEntity.status(401).body(new MessageResponse("Nom d'utilisateur incorrect."));
            }

            if (user.getEtat() != 1) {
                return ResponseEntity.status(401).body(new MessageResponse("L'utilisateur n'a pas encore été accepté."));
            }

            if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body(new MessageResponse("Mot de passe incorrect."));
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
        e.printStackTrace(); // Affichez l'erreur dans la console pour le débogage
        return ResponseEntity.status(500).body(new MessageResponse("Une erreur est survenue lors de la connexion: " + e.getMessage()));
    }
    }
    @PostMapping("/users")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            // Vérifiez si l'utilisateur existe déjà
            User existingUser = userRepository.findByUsername(signUpRequest.getUsername());

            if (existingUser != null) {
                return ResponseEntity.status(400).body(new MessageResponse("Cet utilisateur existe déjà."));
            }

            // Créez un nouvel utilisateur
            User newUser = new User();
            newUser.setUsername(signUpRequest.getUsername());
            newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            newUser.setEmail(signUpRequest.getEmail());
            newUser.setNumInscrit(signUpRequest.getNumInscrit());
            newUser.setUserClasse(signUpRequest.getUserClasse());

            // Enregistrez le nouvel utilisateur
            userRepository.save(newUser);

            return ResponseEntity.status(201).body(new MessageResponse("Inscription réussie"));
        }catch (Exception e) {
            e.printStackTrace(); // Affichez l'erreur dans la console pour le débogage
            return ResponseEntity.status(500).body(new MessageResponse("Une erreur est survenue lors de l'inscription: " + e.getMessage()));
        }
    }

    @GetMapping("/user-statistics")
    public ResponseEntity<?> getUserStatistics() {
        try {
            // Comptez le nombre total d'utilisateurs et d'utilisateurs actifs
            long totalUsers = userRepository.count();
            long activeUsers = userRepository.countByEtat(1);

            return ResponseEntity.ok(new UserStatisticsResponse(totalUsers, activeUsers));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Une erreur est survenue lors de la récupération des statistiques d'utilisateur."));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            // Implémentez la logique pour récupérer tous les utilisateurs
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/api/users/accept/{userId}")
    public ResponseEntity<?> acceptUser(@PathVariable Long userId) {
        try {
            // Implémentez la logique pour accepter l'utilisateur
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setEtat(1);
                userRepository.save(user);
                return ResponseEntity.ok(new MessageResponse("L'utilisateur a été accepté avec succès."));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Une erreur est survenue lors de l'acceptation de l'utilisateur."));
        }
    }

    @DeleteMapping("/api/users/reject/{userId}")
    public ResponseEntity<?> rejectUser(@PathVariable Long userId) {
        try {
            // Implémentez la logique pour refuser l'utilisateur
            userRepository.deleteById(userId);
            return ResponseEntity.ok(new MessageResponse("L'utilisateur a été refusé avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Une erreur est survenue lors du refus de l'utilisateur."));
        }
    }

    private String generateAuthToken(User user) {
        // Générez et retournez le jeton d'authentification
        return Jwts.builder()
                .setSubject(user.getUsername()) // Utilisez le nom d'utilisateur comme sujet du token
                .claim("username", user.getUsername()) // Ajoutez d'autres revendications au besoin
                .signWith(SignatureAlgorithm.HS256, "votre_clé_secrète") // Utilisez une clé secrète pour signer le token
                .compact();
    }

}
