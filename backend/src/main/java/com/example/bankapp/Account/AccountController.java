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
    private final AccountService accountService;
    private final AuthenticationService authenticationService;


    public AccountController(AccountService accountService, AuthenticationService authenticationService) {
        this.accountService = accountService;
        this.authenticationService = authenticationService;
    }
    
    @GetMapping()
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
    
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        boolean authenticated = authenticationService.authenticate(loginRequest.idAccount(), loginRequest.password());
        if (authenticated) {
            AuthDto authDto = new AuthDto(
                    loginRequest.idAccount(),
                    authenticationService.createToken(loginRequest.idAccount())
            );
            return ResponseEntity.ok(authDto);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("Login error. Check email and password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    
    @GetMapping("/{idAccount}")
    public ResponseEntity<AccountDto> getAccountDetails(@PathVariable Long idAccount) {
        Optional<AccountDto> accountDto = accountService.getAccountDetailsById(idAccount);

        return accountDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}
