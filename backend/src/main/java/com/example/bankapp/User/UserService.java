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
        User user = createUser(userRegisterDTO);
        return accountService.generateAccount(userRegisterDTO, user).getIdAccount();
    }

    private User createUser(UserRegisterDto userRegisterDTO) {
        if (!userRepository.existsById(userRegisterDTO.personalId())) {
            User user = UserMapper.INSTANCE.userRegisterDTOtoUser(userRegisterDTO);
            return userRepository.save(user);
        } else {
            return userRepository.findById(userRegisterDTO.personalId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
