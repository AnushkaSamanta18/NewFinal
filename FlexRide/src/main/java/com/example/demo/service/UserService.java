
//logger

package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepo;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepo userRepo;

    public UserEntity saveUser(UserEntity user) {
        logger.info("Saving user: {}", user);
        return userRepo.save(user);
    }

    public boolean validateUser(String email, String password) {
        logger.info("Validating user with email: {}", email);
        UserEntity user = userRepo.findByEmail(email);
        boolean isValid = user != null && user.getPassword().equals(password);
        if (isValid) {
            logger.info("User validation successful");
        } else {
            logger.warn("Invalid user credentials");
        }
        return isValid;
    }

    public Integer getUserId(String email, String password) {
        logger.info("Fetching user ID for email: {}", email);
        UserEntity user = userRepo.findByEmail(email);
        return user != null ? user.getId() : null;
    }

    public UserEntity getUserById(int id) {
        logger.info("Fetching user with ID: {}", id);
        Optional<UserEntity> userOptional = userRepo.findById(id);
        return userOptional.orElse(null);
    }
}

