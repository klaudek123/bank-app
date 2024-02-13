package com.example.bankapp.Account;

import com.example.bankapp.User.User;
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
@CrossOrigin(origins = "*")
public class AccountController {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountRepository accountRepository, AccountService accountService, UserService userService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.userService = userService;
    }


    @GetMapping()
    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest){
        Map<String, String> response = new HashMap<>();
        if(accountService.authenticateLogin(loginRequest.getIdAccount(), loginRequest.getPassword())){
            response.put("message", "Zalogowano pomyślnie!");
            System.out.println(ResponseEntity.status(HttpStatus.OK).body(response));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response.put("error", "Błąd logowania. Sprawdź email i hasło.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/{idAccount}")
    public ResponseEntity<Optional<Account>> getUserDetails(@PathVariable Long idAccount){
        Optional<Account> account = accountService.getAccountDetailsByIdAccount(idAccount);
        if (account.isPresent()) {
            return new ResponseEntity<>(account,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
