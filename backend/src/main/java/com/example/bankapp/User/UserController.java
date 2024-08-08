package com.example.bankapp.User;

import com.example.bankapp.Account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping()
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDTO) {
        Long accountId = userService.registerUser(userRegisterDTO);
        return new ResponseEntity<>(accountId, HttpStatus.CREATED);
    }

    @GetMapping("/{idAccount}")
    public ResponseEntity<UserDto> getAccountDetails(@PathVariable Long idAccount) {
        Long personalId = accountService.getIdUserByIdAccount(idAccount);
        Optional<UserDto> userDto = userService.getAccountDetailsByPersonalId(personalId);

        return userDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
