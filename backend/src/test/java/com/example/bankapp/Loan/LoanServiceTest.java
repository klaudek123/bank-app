package com.example.bankapp.Loan;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.LoanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanService loanService;

    private LoanDto loanDto;
    private Loan loan;
    private Account account;

    @BeforeEach
    public void setUp() {
        loanDto = new LoanDto(
                1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                LocalDateTime.now(), LocalDateTime.now().plusMonths(12), "1", 1L
        );

        account = new Account();
        account.setIdAccount(1L);

        loan = new Loan();
        loan.setIdLoan(1L);
        loan.setAmount(BigDecimal.valueOf(1000));
        loan.setInterestRate(BigDecimal.valueOf(0.05));
        loan.setStartDate(LocalDateTime.now());
        loan.setEndDate(LocalDateTime.now().plusMonths(12));
        loan.setStatus("1");
        loan.setAccount(account);
    }

    @Test
    public void testCreateLoan() {
        // Arrange
        when(accountService.getAccountById(anyLong())).thenReturn(account);
        when(loanMapper.toEntity(any(LoanDto.class))).thenReturn(loan);

        // Act
        loanService.createLoan(loanDto);

        // Assert
        verify(accountService, times(1)).grantLoan(eq(1L), eq(BigDecimal.valueOf(1000)));
        verify(loanRepository, times(1)).save(any(Loan.class));
    }


    @Test
    public void testGetLoanDetailsByIdAccount_Success() {
        // Arrange
        when(loanRepository.findByAccount_IdAccountAndStatus(anyLong(), eq("1"))).thenReturn(Optional.of(loan));
        when(loanMapper.toDto(any(Loan.class))).thenReturn(loanDto);

        // Act
        Optional<LoanDto> result = loanService.getLoanDetailsByIdAccount(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(loanDto, result.get());
    }

    @Test
    public void testGetLoanDetailsByIdAccount_NotFound() {
        // Arrange
        when(loanRepository.findByAccount_IdAccountAndStatus(anyLong(), eq("1"))).thenReturn(Optional.empty());

        // Act
        Optional<LoanDto> result = loanService.getLoanDetailsByIdAccount(1L);

        // Assert
        assertFalse(result.isPresent());
    }
}
