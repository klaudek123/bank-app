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
    public void testGetAccounts() {
        // Arrange
        List<Account> accounts = List.of(new Account(), new Account());
        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = accountController.getAccounts();

        // Assert
        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    public void testLogIn_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(1L, "password");
        when(authenticationService.authenticate(loginRequest.idAccount(), loginRequest.password())).thenReturn(true);
        when(authenticationService.createToken(loginRequest.idAccount())).thenReturn("token");

        // Act
        ResponseEntity<Object> response = accountController.logIn(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthDto);
        AuthDto authDto = (AuthDto) response.getBody();
        assertEquals(loginRequest.idAccount(), authDto.idAccount());
        assertEquals("token", authDto.token());
        verify(authenticationService, times(1)).authenticate(loginRequest.idAccount(), loginRequest.password());
        verify(authenticationService, times(1)).createToken(loginRequest.idAccount());
    }

    @Test
    public void testLogIn_Failure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(1L, "wrongPassword");
        when(authenticationService.authenticate(loginRequest.idAccount(), loginRequest.password())).thenReturn(false);

        // Act
        ResponseEntity<Object> response = accountController.logIn(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Błąd logowania. Sprawdź email i hasło.", errorResponse.error());
        verify(authenticationService, times(1)).authenticate(loginRequest.idAccount(), loginRequest.password());
        verify(authenticationService, never()).createToken(any());
    }

    @Test
    public void testGetUserDetails_Found() {
        // Arrange
        Long idAccount = 1L;
        AccountDto accountDto = new AccountDto(1L, BigDecimal.valueOf(10000), LocalDateTime.now(), "1");
        when(accountService.getAccountDetailsByIdAccount(idAccount)).thenReturn(Optional.of(accountDto));

        // Act
        ResponseEntity<Optional<AccountDto>> response = accountController.getUserDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(accountDto, response.getBody().get());
        verify(accountService, times(1)).getAccountDetailsByIdAccount(idAccount);
    }

    @Test
    public void testGetUserDetails_NotFound() {
        // Arrange
        Long idAccount = 1L;
        when(accountService.getAccountDetailsByIdAccount(idAccount)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Optional<AccountDto>> response = accountController.getUserDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService, times(1)).getAccountDetailsByIdAccount(idAccount);
    }
}
