package com.example.bankapp.Account;

import com.example.bankapp.User.User;
import com.example.bankapp.User.UserRegisterDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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

    public Optional<Account> getAccountDetailsByIdAccount(Long idAccount) {
        return accountRepository.findById(idAccount);
    }
}
