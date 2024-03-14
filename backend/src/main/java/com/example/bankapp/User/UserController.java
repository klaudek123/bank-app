package com.example.bankapp.User;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
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

    public UserController(UserRepository userRepository, UserService userService, AccountService accountService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @PostMapping()
    public ResponseEntity<Long> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (!userRepository.existsById(userRegisterDTO.getPersonalId())) {
            User user = new User();
            user.setPersonalId(userRegisterDTO.getPersonalId());
            user.setFirstname(userRegisterDTO.getFirstname());
            user.setLastname(userRegisterDTO.getLastname());
            user.setDateOfBirth(userRegisterDTO.getDateOfBirth());
            user.setEmail(userRegisterDTO.getEmail());
            user.setAddress(userRegisterDTO.getAddress());
            userRepository.save(user);
        }

        Account account = accountService.generateAccount(userRegisterDTO);

        return new ResponseEntity<>(account.getIdAccount(), HttpStatus.CREATED);
    }

    @GetMapping("/{idAccount}")
    public ResponseEntity<Optional<User>> getUserDetails(@PathVariable Long idAccount){
        try {
            if (idAccount == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Optional<User> user = userService.getUserDetailsByPersonalId(accountService.getIdUserByIdAccount(idAccount));
            if (user.isPresent()) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
