package com.example.bankapp.Transfer;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Mappers.TransferMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTransfersByAccount_SentTransfers() {
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0),
                BigDecimal.valueOf(100)
        );
        when(transferService.getTransfersBySender(idAccount)).thenReturn(List.of(transferDto));

        ResponseEntity<List<TransferDto>> response = transferController.getTransfersByAccount(idAccount, "sent");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(transferService, times(1)).getTransfersBySender(idAccount);
        verify(transferService, never()).getTransfersByRecipient(idAccount);
        verify(transferService, never()).getTransfersByAccount(idAccount);
    }

    @Test
    public void testGetTransfersByAccount_ReceivedTransfers() {
        Long idAccount = 1L;

        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0), // Provide both date and time
                BigDecimal.valueOf(100)
        );

        when(transferService.getTransfersByRecipient(idAccount)).thenReturn(List.of(transferDto));

        ResponseEntity<List<TransferDto>> response = transferController.getTransfersByAccount(idAccount, "received");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(transferService, times(1)).getTransfersByRecipient(idAccount);
        verify(transferService, never()).getTransfersBySender(idAccount);
        verify(transferService, never()).getTransfersByAccount(idAccount);
    }

    @Test
    public void testGetTransfersByAccount_AllTransfers() {
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0), // Provide both date and time
                BigDecimal.valueOf(100)
        );

        when(transferService.getTransfersByAccount(idAccount)).thenReturn(List.of(transferDto));

        ResponseEntity<List<TransferDto>> response = transferController.getTransfersByAccount(idAccount, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(transferService, times(1)).getTransfersByAccount(idAccount);
        verify(transferService, never()).getTransfersBySender(idAccount);
        verify(transferService, never()).getTransfersByRecipient(idAccount);
    }

    @Test
    public void testGetTransferDetails_TransferFound() {
        Long idAccount = 1L;
        Long idTransfer = 2L;
        Transfer transfer = new Transfer();
        transfer.setIdTransfer(idTransfer);
        transfer.setAccount(new Account());
        transfer.getAccount().setIdAccount(idAccount);

        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0), // Provide both date and time
                BigDecimal.valueOf(100)
        );
        when(transferService.getTransferById(idTransfer)).thenReturn(Optional.of(transfer));
        when(transferMapper.toDto(transfer)).thenReturn(transferDto);

        ResponseEntity<TransferDto> response = transferController.getTransferDetails(idAccount, idTransfer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transferDto, response.getBody());
        verify(transferService, times(1)).getTransferById(idTransfer);
        verify(transferMapper, times(1)).toDto(transfer);
    }

    @Test
    public void testGetTransferDetails_TransferNotFound() {
        Long idAccount = 1L;
        Long idTransfer = 2L;

        when(transferService.getTransferById(idTransfer)).thenReturn(Optional.empty());

        ResponseEntity<TransferDto> response = transferController.getTransferDetails(idAccount, idTransfer);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transferService, times(1)).getTransferById(idTransfer);
        verify(transferMapper, never()).toDto(any(Transfer.class));
    }

    @Test
    public void testMakeTransfer_ValidTransfer() {
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0), // Provide both date and time
                BigDecimal.valueOf(100)
        );
        ResponseEntity<String> response = transferController.makeTransfer(idAccount, transferDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transfer made successfully", response.getBody());
        verify(transferService, times(1)).makeTransfer(idAccount, transferDto);
    }

    @Test
    public void testMakeTransfer_SameSenderAndRecipient() {
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                123L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0),
                BigDecimal.valueOf(100)
        );
        ResponseEntity<String> response = transferController.makeTransfer(idAccount, transferDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Sender and recipient cannot be the same", response.getBody());
        verify(transferService, never()).makeTransfer(idAccount, transferDto);
    }

    @Test
    public void testMakeTransfer_NegativeAmount() {
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0), // Provide both date and time
                BigDecimal.valueOf(-5)
        );

        ResponseEntity<String> response = transferController.makeTransfer(idAccount, transferDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Transfer amount must be greater than zero", response.getBody());
        verify(transferService, never()).makeTransfer(idAccount, transferDto);
    }
}