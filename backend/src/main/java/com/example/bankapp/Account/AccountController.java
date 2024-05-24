package com.example.bankapp.Account;

import com.example.bankapp.Config.UserAuthenticationProvider;
import com.example.bankapp.User.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public AccountController(AccountRepository accountRepository, AccountService accountService, UserService userService, UserAuthenticationProvider userAuthenticationProvider) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.userService = userService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    // Method to retrieve all accounts
    @GetMapping()
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    // Method to authenticate user login
    @PostMapping("/login")
    public ResponseEntity<Object> logIn(@RequestBody LoginRequest loginRequest) {
//        System.out.println(loginRequest.toString());
        Map<String, String> response = new HashMap<>();
        if (accountService.authenticateLogin(loginRequest.getIdAccount(), loginRequest.getPassword())) {
            AuthDto authDto = new AuthDto(loginRequest.getIdAccount(), userAuthenticationProvider.createToken(loginRequest.getIdAccount()));

            return ResponseEntity.status(HttpStatus.OK).body(authDto);
        } else {
            response.put("error", "Błąd logowania. Sprawdź email i hasło.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Method to get account details by account ID
    @GetMapping("/{idAccount}")
    public ResponseEntity<Optional<AccountDto>> getUserDetails(@PathVariable Long idAccount) {
        Optional<AccountDto> account = accountService.getAccountDetailsByIdAccount(idAccount);
        if (account.isPresent()) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
