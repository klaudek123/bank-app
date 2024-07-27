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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
        LoanDto loanDto = new LoanDto(
                1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                LocalDateTime.now(), LocalDateTime.now().plusMonths(12), "1", 1L
        );
        when(loanRepository.existsByAccount_IdAccountAndStatus(loanDto.getIdAccount(), "1")).thenReturn(false);

        // Act
        ResponseEntity<String> response = loanController.createLoan(loanDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Loan has been granted!", response.getBody());
        verify(loanService, times(1)).createLoan(loanDto);
    }

    @Test
    public void testCreateLoan_Conflict() {
        // Arrange
        LoanDto loanDto = new LoanDto(
                1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                LocalDateTime.now(), LocalDateTime.now().plusMonths(12), "1", 1L
        );
        when(loanRepository.existsByAccount_IdAccountAndStatus(loanDto.getIdAccount(), "1")).thenReturn(true);

        // Act
        ResponseEntity<String> response = loanController.createLoan(loanDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already has an active loan!", response.getBody());
        verify(loanService, never()).createLoan(loanDto);
    }

    @Test
    public void testGetLoanDetails_Success() {
        // Arrange
        Long idAccount = 1L;
        LoanDto loanDto = new LoanDto(
                1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                LocalDateTime.now(), LocalDateTime.now().plusMonths(12), "1", 1L
        );
        when(loanService.getLoanDetailsByIdAccount(idAccount)).thenReturn(Optional.of(loanDto));

        // Act
        ResponseEntity<Optional<LoanDto>> response = loanController.getLoanDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(loanDto, response.getBody().get());
    }

    @Test
    public void testGetLoanDetails_NotFound() {
        // Arrange
        Long idAccount = 1L;
        when(loanService.getLoanDetailsByIdAccount(idAccount)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Optional<LoanDto>> response = loanController.getLoanDetails(idAccount);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
