package com.example.sprint1.Dao;

import com.example.sprint1.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {
    User findByUsername(String username);
    long countByEtat(int etat);
}

