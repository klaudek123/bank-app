package com.example.bankapp.Account;

import com.example.bankapp.Mappers.AccountMapper;
import com.example.bankapp.User.User;
import com.example.bankapp.User.UserRegisterDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testGenerateAccount() {
        // Arrange
        User user = new User();
        UserRegisterDto userRegisterDto = new UserRegisterDto(1L, "John", "Doe", LocalDate.of(2000, 3, 2), "john.doe@example.com", "poznan", "password");

        // Tworzymy obiekt Account i ustawiamy idAccount
        Account account = new Account();
        account.setIdAccount(123L); // Ustawiamy idAccount ręcznie
        account.setNumber(1000000001L);
        account.setPassword("password");
        account.setBalance(BigDecimal.valueOf(10000));
        account.setType("toChange");
        account.setStatus("1");
        account.setUser(user);

        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountRepository.count()).thenReturn(0L);

        // Act
        Account result = accountService.generateAccount(userRegisterDto, user);

        // Assert
        assertNotNull(result);
        assertEquals(123L, result.getIdAccount());
        assertEquals(BigDecimal.valueOf(10000), result.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testGenerateUserNumber_NoAccounts() {
        // Arrange
        when(accountRepository.count()).thenReturn(0L);

        // Act
        Long result = accountService.generateUserNumber();

        // Assert
        assertEquals(1000000001L, result);
    }

    @Test
    public void testGenerateUserNumber_WithAccounts() {
        // Arrange
        when(accountRepository.count()).thenReturn(5L);
        when(accountRepository.findTopByNumber()).thenReturn(1000000005L);

        // Act
        Long result = accountService.generateUserNumber();

        // Assert
        assertEquals(1000000006L, result);
    }

    @Test
    public void testAuthenticateLogin_ValidCredentials() {
        // Arrange
        Long idAccount = 1L;
        String password = "password";
        when(accountRepository.findByIdAccountAndPassword(idAccount, password)).thenReturn(Optional.of(new Account()));

        // Act
        boolean result = accountService.authenticateLogin(idAccount, password);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAuthenticateLogin_InvalidCredentials() {
        // Arrange
        Long idAccount = 1L;
        String password = "wrongPassword";
        when(accountRepository.findByIdAccountAndPassword(idAccount, password)).thenReturn(Optional.empty());

        // Act
        boolean result = accountService.authenticateLogin(idAccount, password);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetIdUserByIdAccount() {
        // Arrange
        Long idAccount = 1L;
        Long expectedUserId = 2L;
        when(accountRepository.getIdUserByIdAccount(idAccount)).thenReturn(expectedUserId);

        // Act
        Long result = accountService.getIdUserByIdAccount(idAccount);

        // Assert
        assertEquals(expectedUserId, result);
    }

    @Test
    public void testGetAccountDetailsByIdAccount() {
        // Arrange
        Long idAccount = 1L;
        Account account = new Account();
        AccountDto accountDto = new AccountDto(
                account.getNumber(),
                account.getBalance(),
                account.getDateOfCreation(),
                account.getStatus()
        );
        when(accountRepository.findById(idAccount)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        // Act
        Optional<AccountDto> result = accountService.getAccountDetailsByIdAccount(idAccount);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(accountDto, result.get());
    }

    @Test
    public void testTransferFunds() {
        // Arrange
        Long idAccountSender = 1L;
        Long idAccountRecipient = 2L;
        BigDecimal amount = BigDecimal.valueOf(1000);
        Account sender = new Account();
        sender.setBalance(BigDecimal.valueOf(5000));
        Account recipient = new Account();
        recipient.setBalance(BigDecimal.valueOf(3000));
        when(accountRepository.findById(idAccountSender)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(idAccountRecipient)).thenReturn(Optional.of(recipient));

        // Act
        accountService.transferFunds(idAccountSender, idAccountRecipient, amount);

        // Assert
        assertEquals(BigDecimal.valueOf(4000), sender.getBalance());
        assertEquals(BigDecimal.valueOf(4000), recipient.getBalance());
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    public void testMakeLoan() {
        // Arrange
        Long idAccount = 1L;
        BigDecimal loanAmount = BigDecimal.valueOf(1000);
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(5000));
        when(accountRepository.findById(idAccount)).thenReturn(Optional.of(account));

        // Act
        accountService.makeLoan(idAccount, loanAmount);

        // Assert
        assertEquals(BigDecimal.valueOf(6000), account.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testHasSufficientInvestmentBalance_EnoughBalance() {
        // Arrange
        Long idAccount = 1L;
        BigDecimal amount = BigDecimal.valueOf(1000);
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(5000));
        when(accountRepository.findById(idAccount)).thenReturn(Optional.of(account));

        // Act
        boolean result = accountService.hasSufficientInvestmentBalance(idAccount, amount);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSufficientInvestmentBalance_NotEnoughBalance() {
        // Arrange
        Long idAccount = 1L;
        BigDecimal amount = BigDecimal.valueOf(10000);
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(5000));
        when(accountRepository.findById(idAccount)).thenReturn(Optional.of(account));

        // Act
        boolean result = accountService.hasSufficientInvestmentBalance(idAccount, amount);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testMakeInvestment() {
        // Arrange
        Long idAccount = 1L;
        BigDecimal investmentAmount = BigDecimal.valueOf(1000);
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(5000));
        when(accountRepository.findById(idAccount)).thenReturn(Optional.of(account));

        // Act
        accountService.makeInvestment(idAccount, investmentAmount);

        // Assert
        assertEquals(BigDecimal.valueOf(4000), account.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testGetAccountById() {
        // Arrange
        Long idAccount = 1L;
        Account account = new Account();
        when(accountRepository.getAccountByIdAccount(idAccount)).thenReturn(account);

        // Act
        Account result = accountService.getAccountById(idAccount);

        // Assert
        assertEquals(account, result);
    }

}
