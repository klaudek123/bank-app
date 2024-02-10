package com.example.bankapp.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*") // Pozwala na dostęp z dowolnego źródła
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userRepository.existsById(user.getPersonalId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Email already exists
        }
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

}
