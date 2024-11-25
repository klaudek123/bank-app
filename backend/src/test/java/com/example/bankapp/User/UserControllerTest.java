package com.example.bankapp.User;


import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;


    @Test
    public void testGetAccountDetails_ReturnsUser_WhenUserExists() throws Exception {
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

        // Create an Account instance and set the user
        Account account = new Account();
        account.setIdAccount(idAccount);
        account.setUser(user);

        // Mock the accountService and userService methods
        when(accountService.getIdUserByIdAccount(idAccount)).thenReturn(personalId);
        when(userService.getAccountDetailsByPersonalId(personalId)).thenReturn(Optional.of(userDTO));

        // Act and Assert
        mockMvc.perform(get("/users/" + idAccount))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalId").value(personalId))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.address").value("poznan"));
    }

    @Test
    public void testGetAccountDetails_ReturnsNotFound_WhenUserDoesNotExist() throws Exception {
        // Arrange
        Long idAccount = 123L;

        when(accountService.getIdUserByIdAccount(idAccount)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(get("/users/" + idAccount))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("", result.getResponse().getContentAsString()));
    }

    @Test
    public void testGetAccountDetails_NoIdAccount_ReturnsBadRequest() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/users/null"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("", result.getResponse().getContentAsString()));
    }

    @Test
    public void testGetAccountDetails_ExceptionThrown_ReturnsInternalServerError() throws Exception {
        // Arrange
        when(accountService.getIdUserByIdAccount(any())).thenThrow(new RuntimeException("An unexpected error occurred"));

        // Act and Assert
        MvcResult result = mockMvc.perform(get("/users/123"))
                .andExpect(status().isInternalServerError())
                .andReturn();

        assertEquals("An unexpected error occurred", result.getResponse().getContentAsString());
    }

    @Test
    public void testGetAccountDetails_EmptyUser_ReturnsNotFound() throws Exception {
        // Arrange
        when(accountService.getIdUserByIdAccount(any())).thenReturn(1L);
        when(userService.getAccountDetailsByPersonalId(any())).thenReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(get("/users/123"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("", result.getResponse().getContentAsString()));
    }

    @Test
    public void testGetUsers_ReturnsAllUsers() throws Exception {
        // Arrange
        List<User> expectedUserList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(expectedUserList);

        // Act and Assert
        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedUserList.toString(), result.getResponse().getContentAsString());
    }
}
