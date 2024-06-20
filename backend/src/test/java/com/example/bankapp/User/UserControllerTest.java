package com.example.bankapp.User;


import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
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
        Long personalId = 1L;
        Long idAccount = 123L;

        // Create a User instance
        User user = new User();
        user.setPersonalId(personalId);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john.doe@example.com");
        user.setAddress("poznan");

        // Create a UserDTO instance using the mapper
        UserDto userDTO = UserMapper.INSTANCE.userToUserDTO(user);
        Optional<UserDto> userList = Optional.of(userDTO);

        // Create an Account instance and set the user
        Account account = new Account();
        account.setIdAccount(idAccount);
        account.setUser(user);

        // Mock the accountService and userService methods
        when(accountService.getIdUserByIdAccount(idAccount)).thenReturn(personalId);
        when(userService.getUserDetailsByPersonalId(personalId)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Optional<UserDto>> response = userController.getUserDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    public void testGetUserDetails_ReturnsNotFound_WhenUserDoesNotExist() {
        // Arrange
        Long idAccount = 123L;

        when(accountService.getIdUserByIdAccount(idAccount)).thenReturn(null);

        // Act
        ResponseEntity<Optional<UserDto>> response = userController.getUserDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test sprawdzający obsługę żądania GET bez parametru
    @Test
    public void testGetUserDetails_NoIdAccount_ReturnsBadRequest() {
        // Act
        ResponseEntity<Optional<UserDto>> response = userController.getUserDetails(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test sprawdzający obsługę wyjątków - TODO
    @Test
    public void testGetUserDetails_ExceptionThrown_ReturnsInternalServerError() {
        // Arrange
        when(accountService.getIdUserByIdAccount(any())).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<Optional<UserDto>> response = userController.getUserDetails(123L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test sprawdzający zachowanie kontrolera dla różnych przypadków
    @Test
    public void testGetUserDetails_EmptyUser_ReturnsNotFound() {
        // Arrange
        when(accountService.getIdUserByIdAccount(any())).thenReturn(1L);
        when(userService.getUserDetailsByPersonalId(any())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Optional<UserDto>> response = userController.getUserDetails(123L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void testGetUsers_ReturnsAllUsers() {
        // Arrange
        List<User> expectedUserList = new ArrayList<>();


        when(userRepository.findAll()).thenReturn(expectedUserList);

        // Act
        List<User> actualUserList = userController.getUsers();

        // Assert
        assertEquals(expectedUserList, actualUserList);
    }

}
