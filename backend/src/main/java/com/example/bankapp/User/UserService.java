package com.example.bankapp.User;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    // Constructor initializing UserService with UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method to retrieve user details by personal ID
    public Optional<User> getUserDetailsByPersonalId(Long personalId) {
        return userRepository.findById(personalId);
    }
}
