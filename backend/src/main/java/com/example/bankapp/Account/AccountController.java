package com.example.bankapp.Account;

import com.example.bankapp.Auth.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AuthenticationService authenticationService;


    public AccountController(AccountRepository accountRepository, AccountService accountService, AuthenticationService authenticationService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.authenticationService = authenticationService;
    }

    // Method to retrieve all accounts
    @GetMapping()
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    // Method to authenticate user login
    @PostMapping("/login")
    public ResponseEntity<Object> logIn(@RequestBody LoginRequest loginRequest) {
        if (authenticationService.authenticate(loginRequest.idAccount(), loginRequest.password())) {
            AuthDto authDto = new AuthDto(
                    loginRequest.idAccount(),
                    authenticationService.createToken(loginRequest.idAccount())
            );
            return ResponseEntity.status(HttpStatus.OK).body(authDto);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("Błąd logowania. Sprawdź email i hasło.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
