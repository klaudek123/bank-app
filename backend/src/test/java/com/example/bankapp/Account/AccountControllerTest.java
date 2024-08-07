package com.example.bankapp.Account;

import com.example.bankapp.Auth.AuthDto;
import com.example.bankapp.Auth.AuthenticationService;
import com.example.bankapp.Auth.ErrorResponse;
import com.example.bankapp.Auth.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAccounts() {
        // Arrange
        List<Account> accounts = List.of(new Account(), new Account());
        when(accountService.getAllAccounts()).thenReturn(accounts);

        // Act
        List<Account> result = accountController.getAllAccounts();

        // Assert
        assertEquals(2, result.size());
        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(1L, "password");
        when(authenticationService.authenticate(loginRequest.idAccount(), loginRequest.password())).thenReturn(true);
        when(authenticationService.createToken(loginRequest.idAccount())).thenReturn("token");

        // Act
        ResponseEntity<Object> response = accountController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(AuthDto.class, response.getBody());
        AuthDto authDto = (AuthDto) response.getBody();
        assertEquals(loginRequest.idAccount(), authDto.idAccount());
        assertEquals("token", authDto.token());
        verify(authenticationService, times(1)).authenticate(loginRequest.idAccount(), loginRequest.password());
        verify(authenticationService, times(1)).createToken(loginRequest.idAccount());
    }

    @Test
    public void testLogin_Failure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(1L, "wrongPassword");
        when(authenticationService.authenticate(loginRequest.idAccount(), loginRequest.password())).thenReturn(false);

        // Act
        ResponseEntity<Object> response = accountController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Login error. Check email and password.", errorResponse.error());
        verify(authenticationService, times(1)).authenticate(loginRequest.idAccount(), loginRequest.password());
        verify(authenticationService, never()).createToken(any());
    }

    @Test
    public void testGetAccountDetails_Found() {
        // Arrange
        Long idAccount = 1L;
        AccountDto accountDto = new AccountDto(
                1L,
                1L,
                BigDecimal.valueOf(10000),
                LocalDateTime.now(),
                "default",
                "1");
        when(accountService.getAccountDetailsById(idAccount)).thenReturn(Optional.of(accountDto));

        // Act
        ResponseEntity<AccountDto> response = accountController.getAccountDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(accountDto, response.getBody());
        verify(accountService, times(1)).getAccountDetailsById(idAccount);
    }

    @Test
    public void testGetAccountDetails_NotFound() {
        // Arrange
        Long idAccount = 1L;
        when(accountService.getAccountDetailsById(idAccount)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<AccountDto> response = accountController.getAccountDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService, times(1)).getAccountDetailsById(idAccount);
    }
}
