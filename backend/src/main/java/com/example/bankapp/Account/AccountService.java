package com.example.bankapp.Account;

import com.example.bankapp.Config.AppException;
import com.example.bankapp.Mappers.AccountMapper;
import com.example.bankapp.User.UserRegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public Account generateAccount(UserRegisterDTO userRegisterDTO) {
        Account account = new Account();
        account.setNumber(generateUserNumber());
        account.setPassword(userRegisterDTO.getPassword());
        account.setBalance(1000L); // Assuming initial balance is 0
        account.setStatus("1"); // Assuming account is active by default
        account.setIdUser(userRegisterDTO.getPersonalId()); // Assuming idUser is a String
        accountRepository.save(account);

        return account;
    }

    private Long generateUserNumber() {
        long numberOfAccounts = accountRepository.count(); // Pobierz liczbę kont w bazie danych
        if (numberOfAccounts > 0) {
            long maxNumber = accountRepository.findTopByNumber(); // Pobierz maksymalny numer konta
            return maxNumber + 1; // Zwróć maxNumber + 1
        }
        return 1000000001L; // Jeśli nie ma kont w bazie danych, zwróć 1000001L
    }

    public boolean authenticateLogin(Long idAccount, String password) {
        return accountRepository.findByIdAccountAndPassword(idAccount, password).isPresent();
    }

    public Long getIdUserByIdAccount(Long idAccount){
        return accountRepository.getIdUserByIdAccount(idAccount);
    }

    public Optional<AccountDto> getAccountDetailsByIdAccount(Long idAccount) {
        Optional<Account> accountOptional = accountRepository.findById(idAccount);
        return accountOptional.map(accountMapper::accountToAccountDto);
    }


    public AuthDto findByIdAccount(String idAcc) {
        Account acc = accountRepository.findById(Long.valueOf(idAcc))
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return new AuthDto(acc.getIdAccount(), null);
    }
}
