package com.example.bankapp.Account;

import com.example.bankapp.Config.AppException;
import com.example.bankapp.Mappers.AccountMapper;
import com.example.bankapp.User.UserRegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        account.setBalance(BigDecimal.valueOf(1000)); // Assuming initial balance is 0
        account.setStatus("1"); // Assuming account is active by default
        account.setIdUser(userRegisterDTO.getPersonalId()); // Assuming idUser is a String
        accountRepository.save(account);

        return account;
    }

    private Long generateUserNumber() {
        long numberOfAccounts = accountRepository.count(); // Pobierz liczbę kont w bazie danych
        if (numberOfAccounts > 0) {
            Long maxNumber = accountRepository.findTopByNumber(); // Pobierz maksymalny numer konta
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

    public Long getNumberByIdAccount(Long idAccount) {
        return accountRepository.getNumberByIdAccount(idAccount);
    }

    public Long getIdAccountByNumber(Long recipient) {
        return accountRepository.getIdAccountByNumber(recipient);
    }

    public void transferFunds(Long idAccountSender, Long idAccountRecipient, BigDecimal amount) {
        Optional<Account> senderOptional = accountRepository.findById(idAccountSender);
        Optional<Account> recipientOptional = accountRepository.findById(idAccountRecipient);

        if (senderOptional.isPresent() && recipientOptional.isPresent()) {
            Account senderAccount = senderOptional.get();
            Account recipientAccount = recipientOptional.get();

            BigDecimal senderBalance = senderAccount.getBalance();
            BigDecimal recipientBalance = recipientAccount.getBalance();

            // Odjęcie kwoty od konta sendera
            senderBalance = senderBalance.subtract(amount);
            // Dodanie kwoty do konta recipienta
            recipientBalance = recipientBalance.add(amount);

            senderAccount.setBalance(senderBalance);
            recipientAccount.setBalance(recipientBalance);

            // Zapisz zmiany w bazie danych
            accountRepository.save(senderAccount);
            accountRepository.save(recipientAccount);
        }
    }
    
    public void makeLoan(Long idAccount, BigDecimal amount) {
        Optional<Account> accountOptional = accountRepository.findById(idAccount);
        
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            
            BigDecimal balance = account.getBalance();

            account.setBalance(balance.add(amount));

            accountRepository.save(account);
        }
    }
}
