package com.example.bankapp.Account;

import com.example.bankapp.Auth.AuthDto;
import com.example.bankapp.Config.AppException;
import com.example.bankapp.Mappers.AccountMapper;
import com.example.bankapp.User.User;
import com.example.bankapp.User.UserRegisterDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Account generateAccount(UserRegisterDto userRegisterDTO, User user) {
        Account account = new Account();
        account.setNumber(generateUniqueAccountNumber());
        String encryptedPassword = passwordEncoder.encode(userRegisterDTO.password());
        account.setPassword(encryptedPassword);
        account.setBalance(BigDecimal.valueOf(10000)); // Initial balance set to 10000 PLN
        account.setType("default"); // TODO frontend - add account types
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        return accountRepository.save(account);
    }

    protected Long generateUniqueAccountNumber() {
        Long maxNumber = accountRepository.findMaxAccountNumber();
        return (maxNumber != null) ? maxNumber + 1 : 1000000001L;
    }

    public boolean authenticateLogin(Long idAccount, String password) {
        Optional<Account> accountOptional = accountRepository.findById(idAccount);

        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            return passwordEncoder.matches(password, account.getPassword());
        }
        return false;
    }


    public Long getIdUserByIdAccount(Long idAccount) {
        return accountRepository.getIdUserByIdAccount(idAccount);
    }

    public Optional<AccountDto> getAccountDetailsById(Long idAccount) {
        return accountRepository.findById(idAccount)
                .map(accountMapper::toDto);
    }

    public AuthDto findByIdAccount(String idAcc) {
        Account acc = accountRepository.findById(Long.valueOf(idAcc))
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return new AuthDto(acc.getIdAccount(), null);
    }

    public Long getNumberByIdAccount(Long idAccount) {
        return accountRepository.getNumberByIdAccount(idAccount);
    }

    public Account getAccountByNumber(Long recipient) {
        return accountRepository.getAccountByNumber(recipient);
    }

    public void transferFunds(Long senderId, Long recipientId, BigDecimal amount) {
        Account senderAccount = accountRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account recipientAccount = accountRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

    }

    public void grantLoan(Long idAccount, BigDecimal loanAmount) {
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(loanAmount));
        accountRepository.save(account);
    }

    public boolean hasSufficientBalance(Long idAccount, BigDecimal amount) {
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getBalance().compareTo(amount) >= 0;
    }

    public void makeInvestment(Long idAccount, BigDecimal investmentAmount) {
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().subtract(investmentAmount));
        accountRepository.save(account);
    }

    public Account getAccountById(Long idAccount) {
        return accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
