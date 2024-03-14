package com.example.bankapp.User;


import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.User.User;
import com.example.bankapp.User.UserController;
import com.example.bankapp.User.UserRepository;
import com.example.bankapp.User.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserController userController;


    @Test
    public void testGetUserDetails_ReturnsUser_WhenUserExists() {
        // Arrange
        User user = new User();
        user.setPersonalId(1L);
        user.setAddress("poznan");

        Account account = new Account();
        account.setIdAccount(123L);
        account.setIdUser(1L);

        Optional<User> userOptional = Optional.of(user);
        Long idAccount = 123L;

        when(accountService.getIdUserByIdAccount(idAccount)).thenReturn(account.getIdUser());
        when(userService.getUserDetailsByPersonalId(account.getIdUser())).thenReturn(userOptional);

        // Act
        ResponseEntity<Optional<User>> response = userController.getUserDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userOptional, response.getBody());
    }

    @Test
    public void testGetUserDetails_ReturnsNotFound_WhenUserDoesNotExist() {
        // Arrange
        Long idAccount = 123L;

        when(accountService.getIdUserByIdAccount(idAccount)).thenReturn(null);

        // Act
        ResponseEntity<Optional<User>> response = userController.getUserDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test sprawdzający obsługę żądania GET bez parametru
    @Test
    public void testGetUserDetails_NoIdAccount_ReturnsBadRequest() {
        // Act
        ResponseEntity<Optional<User>> response = userController.getUserDetails(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test sprawdzający obsługę wyjątków
    @Test
    public void testGetUserDetails_ExceptionThrown_ReturnsInternalServerError() {
        // Arrange
        when(accountService.getIdUserByIdAccount(any())).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<Optional<User>> response = userController.getUserDetails(123L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test sprawdzający zachowanie kontrolera dla różnych przypadków
    @Test
    public void testGetUserDetails_EmptyUser_ReturnsNotFound() {
        // Arrange
        when(accountService.getIdUserByIdAccount(any())).thenReturn(1L);
        when(userService.getUserDetailsByPersonalId(any())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Optional<User>> response = userController.getUserDetails(123L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void testGetUsers_ReturnsAllUsers() {
        // Arrange
        List<User> expectedUserList = new ArrayList<>();
        // Dodaj kilka użytkowników do listy expectedUserList

        // Ustaw zachowanie metody findAll z repozytorium, aby zwracała listę użytkowników
        when(userRepository.findAll()).thenReturn(expectedUserList);

        // Act
        List<User> actualUserList = userController.getUsers();

        // Assert
        assertEquals(expectedUserList, actualUserList);
    }

}
