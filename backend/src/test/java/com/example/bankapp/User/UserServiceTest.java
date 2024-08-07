package com.example.bankapp.User;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserService userService;

    @Test
    public void testgetAccountDetailsByPersonalId_UserExists() {
        // Arrange
        Long personalId = 1L;
        User user = new User();
        user.setPersonalId(personalId);
        when(userRepository.findById(personalId)).thenReturn(Optional.of(user));

        // Act
        Optional<UserDto> result = userService.getAccountDetailsByPersonalId(personalId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(personalId, result.get().personalId());
    }

    @Test
    public void testgetAccountDetailsByPersonalId_UserDoesNotExist() {
        // Arrange
        Long personalId = 1L;
        when(userRepository.findById(personalId)).thenReturn(Optional.empty());

        // Act
        Optional<UserDto> result = userService.getAccountDetailsByPersonalId(personalId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void testRegisterUser_NewUser() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto(1L, "John", "Doe", LocalDate.of(2000,3,2),"john.doe@example.com", "poznan", "password");
        User user = UserMapper.INSTANCE.userRegisterDTOtoUser(userRegisterDto);
        Account account = new Account();
        account.setIdAccount(123L);
        when(userRepository.existsById(userRegisterDto.personalId())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(accountService.generateAccount(any(UserRegisterDto.class), any(User.class))).thenReturn(account);

        // Act
        Long accountId = userService.registerUser(userRegisterDto);

        // Assert
        assertEquals(account.getIdAccount(), accountId);
        verify(userRepository, times(1)).save(any(User.class));
        verify(accountService, times(1)).generateAccount(any(UserRegisterDto.class), any(User.class));
    }

    @Test
    public void testRegisterUser_ExistingUser() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto(1L, "John", "Doe" , LocalDate.of(2000,3,2),"john.doe@example.com", "poznan", "password");
        User user = UserMapper.INSTANCE.userRegisterDTOtoUser(userRegisterDto);
        Account account = new Account();
        account.setIdAccount(123L);
        when(userRepository.existsById(userRegisterDto.personalId())).thenReturn(true);
        when(userRepository.findById(userRegisterDto.personalId())).thenReturn(Optional.of(user));
        when(accountService.generateAccount(any(UserRegisterDto.class), any(User.class))).thenReturn(account);

        // Act
        Long accountId = userService.registerUser(userRegisterDto);

        // Assert
        assertEquals(account.getIdAccount(), accountId);
        verify(userRepository, never()).save(any(User.class));
        verify(accountService, times(1)).generateAccount(any(UserRegisterDto.class), any(User.class));
    }

    @Test
    public void testRegisterUser_ExistingUserNotFound() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto(1L, "John", "Doe", LocalDate.of(2000,3,2),"john.doe@example.com", "poznan", "password");
        when(userRepository.existsById(userRegisterDto.personalId())).thenReturn(true);
        when(userRepository.findById(userRegisterDto.personalId())).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(userRegisterDto);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(accountService, never()).generateAccount(any(UserRegisterDto.class), any(User.class));
    }
}