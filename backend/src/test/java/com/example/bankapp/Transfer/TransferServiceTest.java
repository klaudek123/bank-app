package com.example.bankapp.Transfer;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Kafka.KafkaProducer;
import com.example.bankapp.Mappers.TransferMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTransferById() {
        Long idTransfer = 1L;
        Transfer transfer = new Transfer();
        transfer.setIdTransfer(idTransfer);
        when(transferRepository.findById(idTransfer)).thenReturn(Optional.of(transfer));

        Optional<Transfer> result = transferService.getTransferById(idTransfer);

        assertEquals(Optional.of(transfer), result);
        verify(transferRepository, times(1)).findById(idTransfer);
    }

    @Test
    public void testGetTransfersByAccount() {
        Long idAccount = 1L;
        Transfer transfer = new Transfer();
        transfer.setAccount(new Account());
        transfer.getAccount().setIdAccount(idAccount);
        when(transferRepository.findByAccount_IdAccountOrderByDateDesc(idAccount))
                .thenReturn(List.of(transfer));

        List<TransferDto> result = transferService.getTransfersByAccount(idAccount);

        assertEquals(1, result.size());
        verify(transferRepository, times(1)).findByAccount_IdAccountOrderByDateDesc(idAccount);
    }

    @Test
    public void testGetTransfersBySender() {
        Long idAccount = 1L;
        Long accountNumber = 123L;

        Account account = new Account();
        account.setNumber(accountNumber);

        Transfer transfer = new Transfer();
        transfer.setSender(accountNumber);
        when(accountService.getNumberByIdAccount(idAccount)).thenReturn(accountNumber);
        when(transferRepository.findBySenderAndAccount_IdAccount(accountNumber, idAccount))
                .thenReturn(List.of(transfer));

        List<TransferDto> result = transferService.getTransfersBySender(idAccount);

        assertEquals(1, result.size());
        verify(transferRepository, times(1)).findBySenderAndAccount_IdAccount(accountNumber, idAccount);
        verify(accountService, times(1)).getNumberByIdAccount(idAccount);
    }

    @Test
    public void testGetTransfersByRecipient() {
        Long idAccount = 1L;
        Long accountNumber = 123L;

        Account account = new Account();
        account.setNumber(accountNumber);

        Transfer transfer = new Transfer();
        transfer.setRecipient(accountNumber);
        when(accountService.getNumberByIdAccount(idAccount)).thenReturn(accountNumber);
        when(transferRepository.findByRecipientAndAccount_IdAccount(accountNumber, idAccount))
                .thenReturn(List.of(transfer));

        List<TransferDto> result = transferService.getTransfersByRecipient(idAccount);

        assertEquals(1, result.size());
        verify(transferRepository, times(1)).findByRecipientAndAccount_IdAccount(accountNumber, idAccount);
        verify(accountService, times(1)).getNumberByIdAccount(idAccount);
    }

    @Test
    public void testProcessTransfer() {
        Long idAccount = 1L;

        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0),
                BigDecimal.valueOf(100)
        );

        Account senderAccount = new Account();
        senderAccount.setIdAccount(idAccount);
        Account recipientAccount = new Account();
        recipientAccount.setIdAccount(2L);

        when(accountService.getAccountById(idAccount)).thenReturn(senderAccount);

        when(accountService.getAccountByNumber(transferDto.recipient())).thenReturn(recipientAccount);

        when(transferRepository.save(any(Transfer.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        transferService.processTransfer(idAccount, transferDto);

        verify(accountService, times(1)).getAccountById(idAccount);
        verify(accountService, times(1)).getAccountByNumber(456L);
        verify(transferRepository, times(2)).save(any(Transfer.class));
        verify(accountService, times(1)).transferFunds(idAccount, 2L, BigDecimal.valueOf(100));
    }
}
