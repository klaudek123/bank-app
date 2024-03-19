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

    // Method to generate a new account for a user
    public Account generateAccount(UserRegisterDTO userRegisterDTO) {
        Account account = new Account();
        account.setNumber(generateUserNumber()); // Generate a unique account number
        account.setPassword(userRegisterDTO.getPassword());
        account.setBalance(BigDecimal.valueOf(1000)); // Initial balance set to $1000
        account.setStatus("1"); // Status set to active
        account.setIdUser(userRegisterDTO.getPersonalId()); // Set user ID
        accountRepository.save(account); // Save account details to the database

        return account;
    }

    // Method to generate a unique account number
    private Long generateUserNumber() {
        long numberOfAccounts = accountRepository.count(); // Get the number of existing accounts
        if (numberOfAccounts > 0) {
            Long maxNumber = accountRepository.findTopByNumber(); // Get the maximum account number
            return maxNumber + 1; // Return the next available account number
        }
        return 1000000001L; // Return a default account number if no accounts exist
    }

    // Method to authenticate user login
    public boolean authenticateLogin(Long idAccount, String password) {
        return accountRepository.findByIdAccountAndPassword(idAccount, password).isPresent();
    }

    // Method to retrieve user ID by account ID
    public Long getIdUserByIdAccount(Long idAccount) {
        return accountRepository.getIdUserByIdAccount(idAccount);
    }

    // Method to retrieve account details by account ID
    public Optional<AccountDto> getAccountDetailsByIdAccount(Long idAccount) {
        Optional<Account> accountOptional = accountRepository.findById(idAccount);
        return accountOptional.map(accountMapper::accountToAccountDto);
    }

    // Method to find account by ID or throw an exception if not found
    public AuthDto findByIdAccount(String idAcc) {
        Account acc = accountRepository.findById(Long.valueOf(idAcc))
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return new AuthDto(acc.getIdAccount(), null);
    }

    // Method to get account number by account ID
    public Long getNumberByIdAccount(Long idAccount) {
        return accountRepository.getNumberByIdAccount(idAccount);
    }

    // Method to get account ID by account number
    public Long getIdAccountByNumber(Long recipient) {
        return accountRepository.getIdAccountByNumber(recipient);
    }

    // Method to transfer funds from one account to another
    public void transferFunds(Long idAccountSender, Long idAccountRecipient, BigDecimal amount) {
        Optional<Account> senderOptional = accountRepository.findById(idAccountSender);
        Optional<Account> recipientOptional = accountRepository.findById(idAccountRecipient);

        if (senderOptional.isPresent() && recipientOptional.isPresent()) {
            Account senderAccount = senderOptional.get();
            Account recipientAccount = recipientOptional.get();

            BigDecimal senderBalance = senderAccount.getBalance();
            BigDecimal recipientBalance = recipientAccount.getBalance();

            senderBalance = senderBalance.subtract(amount); // Subtract amount from sender's balance
            recipientBalance = recipientBalance.add(amount); // Add amount to recipient's balance


            senderAccount.setBalance(senderBalance);
            recipientAccount.setBalance(recipientBalance);

            // Save changes to the database
            accountRepository.save(senderAccount);
            accountRepository.save(recipientAccount);
        }
    }

    // Method to grant a loan to the account
    public void makeLoan(Long idAccount, BigDecimal loanAmount) {
        Optional<Account> accountOptional = accountRepository.findById(idAccount);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            BigDecimal balance = account.getBalance();

            account.setBalance(balance.add(loanAmount)); // Increase account balance by loan amount

            accountRepository.save(account);
        }
    }

    // Method to check if account has sufficient balance for investment
    public boolean hasSufficientInvestmentBalance(Long idAccount, BigDecimal amount) {
        Account account = accountRepository.findById(idAccount).orElse(null);
        if (account == null) {
            return false;
        }
        return account.getBalance().compareTo(amount) >= 0;
    }

    // Method to make an investment from the account
    public void makeInvestment(Long idAccount, BigDecimal investmentAmount) {
        Optional<Account> accountOptional = accountRepository.findById(idAccount);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            BigDecimal balance = account.getBalance();

            account.setBalance(balance.subtract(investmentAmount)); // Subtract the investment amount from the account balance

            accountRepository.save(account);
        }
    }
}
