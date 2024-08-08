package com.example.bankapp.Investment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.bankapp.Account.AccountService;
import com.example.bankapp.User.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InvestmentControllerTest {

    @Mock
    private InvestmentService investmentService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private InvestmentController investmentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInvestment_ReturnsOk_WhenSufficientBalance() {
        // Arrange
        Long idAccount = 1L;
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        InvestmentDto investmentDto = new InvestmentDto(
                1L, "Investment1", InvestmentType.FUND,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                startDate, endDate, InvestmentStatus.ACTIVE
        );

        when(accountService.hasSufficientBalance(eq(idAccount), eq(investmentDto.amount()))).thenReturn(true);

        // Act
        ResponseEntity<String> response = investmentController.createInvestment(idAccount, investmentDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The investment has been started successfully.", response.getBody());
        verify(investmentService, times(1)).createInvestment(idAccount, investmentDto);
    }

    @Test
    public void testCreateInvestment_ReturnsConflict_WhenInsufficientBalance() {
        // Arrange
        Long idAccount = 1L;
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        InvestmentDto investmentDto = new InvestmentDto(
                1L, "Investment1", InvestmentType.GOLD,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                startDate, endDate, InvestmentStatus.ACTIVE
        );

        when(accountService.hasSufficientBalance(eq(idAccount), eq(investmentDto.amount()))).thenReturn(false);

        // Act
        ResponseEntity<String> response = investmentController.createInvestment(idAccount, investmentDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("There are no funds in the account to make the investment.", response.getBody());
        verify(investmentService, never()).createInvestment(anyLong(),any(InvestmentDto.class));
    }

    @Test
    public void testGetInvestmentDetails_ReturnsOk_WhenFoundInvestments() {
        // Arrange
        Long idAccount = 1L;
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        List<InvestmentDto> investments = List.of(
                new InvestmentDto(
                        1L, "Investment1", InvestmentType.FUND,
                        BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                        startDate, endDate, InvestmentStatus.ACTIVE
                )
        );

        when(investmentService.getInvestmentDetailsByIdAccount(idAccount)).thenReturn(investments);

        // Act
        ResponseEntity<List<InvestmentDto>> response = investmentController.getInvestmentDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(investments, response.getBody());
    }

    @Test
    public void testGetInvestmentDetails_ReturnsNotFound_WhenNotFoundInvestments() {
        // Arrange
        Long idAccount = 1L;
        when(investmentService.getInvestmentDetailsByIdAccount(idAccount)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<InvestmentDto>> response = investmentController.getInvestmentDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
