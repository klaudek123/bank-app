package com.example.bankapp.User;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountService accountService;

    public UserService(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    public Optional<UserDto> getAccountDetailsByPersonalId(Long personalId) {
        return userRepository.findById(personalId)
                .map(UserMapper.INSTANCE::userToUserDTO);
    }

    public Long registerUser(UserRegisterDto userRegisterDTO) {
        User user;

        if (!userRepository.existsById(userRegisterDTO.personalId())) {
            // Create a new user
            user = UserMapper.INSTANCE.userRegisterDTOtoUser(userRegisterDTO);
            userRepository.save(user);
        } else {
            // Fetch the existing user
            user = userRepository.findById(userRegisterDTO.personalId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        // Create a new account for the user
        Account account = accountService.generateAccount(userRegisterDTO, user);

        return account.getIdAccount();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
