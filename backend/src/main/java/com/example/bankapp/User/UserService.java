package com.example.bankapp.User;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserDetailsByPersonalId(Long personalId) {
        return userRepository.findById(personalId);
    }
}
