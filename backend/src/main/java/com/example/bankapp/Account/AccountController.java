package com.example.bankapp.Account;

import com.example.bankapp.Auth.*;
import com.example.bankapp.Config.AppException;
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
    public ResponseEntity<AuthDto> login(@RequestBody LoginRequest loginRequest) {
        if (!authenticationService.authenticate(loginRequest.idAccount(), loginRequest.password())) {
            throw new AppException("Invalid login credentials", HttpStatus.UNAUTHORIZED);
        }

        AuthDto authDto = new AuthDto(
                loginRequest.idAccount(),
                authenticationService.createToken(loginRequest.idAccount())
        );
        return ResponseEntity.ok(authDto);
    }
    
    @GetMapping("/{idAccount}")
    public ResponseEntity<AccountDto> getAccountDetails(@PathVariable Long idAccount) {
        Optional<AccountDto> accountDto = accountService.getAccountDetailsById(idAccount);

        return accountDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}
