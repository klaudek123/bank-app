package com.example.bankapp.User;

import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final AccountService accountService;

    // Constructor initializing UserController with required repository and services
    public UserController(UserRepository userRepository, UserService userService, AccountService accountService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.accountService = accountService;
    }

    // Endpoint to retrieve all users
    @GetMapping("")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Endpoint to register a new user
    @PostMapping()
    public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDTO) {
        Long accountId = userService.registerUser(userRegisterDTO);
        return new ResponseEntity<>(accountId, HttpStatus.CREATED);
    }

    // Endpoint to retrieve user details by account ID
    @GetMapping("/{idAccount}")
    public ResponseEntity<Optional<UserDto>> getUserDetails(@PathVariable Long idAccount) {
        if (idAccount == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<UserDto> userDto = userService.getUserDetailsByPersonalId(accountService.getIdUserByIdAccount(idAccount)).map(user -> UserMapper.INSTANCE.userToUserDTO(user));

        if (userDto.isPresent()) {
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
