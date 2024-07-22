package com.example.bankapp.Investment;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.InvestmentMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class InvestmentServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private InvestmentRepository investmentRepository;

    @InjectMocks
    private InvestmentService investmentService;

    @Mock
    private InvestmentMapper investmentMapper;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }


    @Test
    public void testCreateInvestment_Success() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        InvestmentDto investmentDto = new InvestmentDto(
                1L, "Investment1", InvestmentType.FUND,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                startDate, endDate, InvestmentStatus.ACTIVE, 1L
        );

        Investment investment = new Investment();
        investment.setIdInvestment(1L);
        investment.setAccount(new Account()); // Assuming Account has a default constructor
        investment.getAccount().setIdAccount(1L);
        investment.setAmount(BigDecimal.valueOf(1000));

        when(investmentMapper.toEntity(investmentDto)).thenReturn(investment);
        when(investmentRepository.save(any(Investment.class))).thenReturn(investment);

        // Act
        investmentService.createInvestment(investmentDto);

        // Assert
        verify(investmentRepository, times(1)).save(any(Investment.class));
        verify(accountService, times(1)).makeInvestment(eq(1L), eq(BigDecimal.valueOf(1000)));
    }

    @Test
    public void testGetInvestmentDetailsByIdAccount() {
        // Arrange
        Long idAccount = 1L;
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        Investment investment = new Investment();
        investment.setIdInvestment(1L);
        investment.setName("Investment1");
        investment.setAccount(new Account());
        investment.setType(InvestmentType.FUND);
        investment.setInterestRate(BigDecimal.valueOf(0.05));
        investment.getAccount().setIdAccount(idAccount);
        investment.setAmount(BigDecimal.valueOf(1000));
        investment.setStartDate(startDate);
        investment.setEndDate(endDate);
        investment.setStatus(InvestmentStatus.ACTIVE);

        InvestmentDto investmentDto = new InvestmentDto(
                1L, "Investment1", InvestmentType.FUND,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(0.05),
                startDate, endDate, InvestmentStatus.ACTIVE, idAccount
        );

        when(investmentRepository.findByIdAccountAndStatus(eq(idAccount), eq(InvestmentStatus.ACTIVE)))
                .thenReturn(List.of(investment));
        when(investmentMapper.toDto(any(Investment.class))).thenReturn(investmentDto);

        // Act
        List<InvestmentDto> result = investmentService.getInvestmentDetailsByIdAccount(idAccount);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(investmentDto, result.get(0));
    }

    @Test
    public void testGetInvestmentDetailsByIdAccount_NoInvestments() {
        // Arrange
        Long idAccount = 1L;
        when(investmentRepository.findByIdAccountAndStatus(eq(idAccount), eq(InvestmentStatus.ACTIVE)))
                .thenReturn(List.of());

        // Act
        List<InvestmentDto> result = investmentService.getInvestmentDetailsByIdAccount(idAccount);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}