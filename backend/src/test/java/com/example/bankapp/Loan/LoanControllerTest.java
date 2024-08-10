package com.example.bankapp.Loan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanController loanController;

    @Test
    public void testCreateLoan_Success() {
        // Arrange
        Long idAccount = 1L;
        LoanDto loanDto = new LoanDto(
                1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                LocalDateTime.now(), LocalDateTime.now().plusMonths(12), LoanStatus.ACTIVE
        );
        when(loanRepository.existsByAccount_IdAccountAndStatus(idAccount, LoanStatus.ACTIVE)).thenReturn(false);

        // Act
        ResponseEntity<String> response = loanController.createLoan(idAccount, loanDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Loan has been granted!", response.getBody());
        verify(loanService, times(1)).createLoan(idAccount, loanDto);
    }

    @Test
    public void testCreateLoan_Conflict() {
        // Arrange
        Long idAccount = 1L;
        LoanDto loanDto = new LoanDto(
                1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                LocalDateTime.now(), LocalDateTime.now().plusMonths(12), LoanStatus.ACTIVE
        );
        when(loanRepository.existsByAccount_IdAccountAndStatus(idAccount, LoanStatus.ACTIVE)).thenReturn(true);

        // Act
        ResponseEntity<String> response = loanController.createLoan(idAccount, loanDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already has an active loan!", response.getBody());
        verify(loanService, never()).createLoan(idAccount, loanDto);
    }

    @Test
    public void testGetLoanDetails_Success() {
        // Arrange
        Long idAccount = 1L;
        LoanDto loanDto = new LoanDto(
                1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                LocalDateTime.now(), LocalDateTime.now().plusMonths(12), LoanStatus.ACTIVE
        );
        when(loanService.getLoanDetailsByIdAccount(idAccount)).thenReturn(Optional.of(loanDto));

        // Act
        ResponseEntity<LoanDto> response = loanController.getLoanDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(loanDto, response.getBody());
    }

    @Test
    public void testGetLoanDetails_NotFound() {
        // Arrange
        Long idAccount = 1L;
        when(loanService.getLoanDetailsByIdAccount(idAccount)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<LoanDto> response = loanController.getLoanDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
