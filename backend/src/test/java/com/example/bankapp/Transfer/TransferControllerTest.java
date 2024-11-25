package com.example.bankapp.Transfer;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Auth.AuthDto;
import com.example.bankapp.Auth.AuthenticationService;
import com.example.bankapp.Config.UserAuthenticationProvider;
import com.example.bankapp.Mappers.TransferMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
@WithMockUser
public class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @MockBean
    private TransferMapper transferMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserAuthenticationProvider userAuthenticationProvider;

    @InjectMocks
    private TransferController transferController;

//    @BeforeEach
//    public void setup() {
//        AuthDto mockAuthDto = new AuthDto(1L, "mocked-token");
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                mockAuthDto, null, Collections.emptyList()
//        );
//
//        when(userAuthenticationProvider.validateToken("mocked-token")).thenReturn(authentication);
//    }
//
//    @BeforeEach
//    public void mockTransferService() {
//        doNothing().when(transferService).processTransfer(eq(1L), any(TransferDto.class));
//    }

    @Test
    public void testGetTransfersByAccount_SentTransfers() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(get("/accounts/{idAccount}/transfers?type=sent", idAccount))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTransfer").value(transferDto.idTransfer()))
                .andExpect(jsonPath("$[0].sender").value(transferDto.sender()))
                .andExpect(jsonPath("$[0].recipient").value(transferDto.recipient()))
                .andExpect(jsonPath("$[0].amount").value(transferDto.amount()));

        verify(transferService).getTransfersBySender(idAccount);
        verify(transferService, never()).getTransfersByRecipient(idAccount);
    }

    @Test
    public void testGetTransfersByAccount_ReceivedTransfers() throws Exception {
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0),
                BigDecimal.valueOf(100)
        );
        when(transferService.getTransfersByRecipient(idAccount)).thenReturn(List.of(transferDto));

        mockMvc.perform(get("/accounts/{idAccount}/transfers?type=received", idAccount))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTransfer").value(transferDto.idTransfer()))
                .andExpect(jsonPath("$[0].sender").value(transferDto.sender()))
                .andExpect(jsonPath("$[0].recipient").value(transferDto.recipient()))
                .andExpect(jsonPath("$[0].amount").value(transferDto.amount()));

        verify(transferService, times(1)).getTransfersByRecipient(idAccount);
    }


    @Test
    public void testGetTransfersByAccount_AllTransfers() throws Exception {
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0),
                BigDecimal.valueOf(100)
        );
        when(transferService.getTransfersByAccount(idAccount)).thenReturn(List.of(transferDto));

        mockMvc.perform(get("/accounts/{idAccount}/transfers", idAccount))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTransfer").value(transferDto.idTransfer()))
                .andExpect(jsonPath("$[0].sender").value(transferDto.sender()))
                .andExpect(jsonPath("$[0].recipient").value(transferDto.recipient()))
                .andExpect(jsonPath("$[0].amount").value(transferDto.amount()));

        verify(transferService, times(1)).getTransfersByAccount(idAccount);
    }

    @Test
    public void testGetTransferDetails_TransferFound() throws Exception {
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

        mockMvc.perform(get("/accounts/{idAccount}/transfers/{idTransfer}", idAccount, idTransfer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTransfer").value(transferDto.idTransfer()))
                .andExpect(jsonPath("$.sender").value(transferDto.sender()))
                .andExpect(jsonPath("$.recipient").value(transferDto.recipient()))
                .andExpect(jsonPath("$.amount").value(transferDto.amount()));

        verify(transferService, times(1)).getTransferById(idTransfer);
        verify(transferMapper, times(1)).toDto(transfer);
    }

    @Test
    public void testGetTransferDetails_TransferNotFound() throws Exception {
        Long idAccount = 1L;
        Long idTransfer = 2L;

        when(transferService.getTransferById(idTransfer)).thenReturn(Optional.empty());


        mockMvc.perform(get("/accounts/{idAccount}/transfers/{idTransfer}", idAccount, idTransfer))
                .andExpect(status().isNotFound());

        verify(transferService, times(1)).getTransferById(idTransfer);
        verify(transferMapper, never()).toDto(any(Transfer.class));
    }

    @Test
    public void testProcessTransfer_ValidTransfer() throws Exception {
        Long idAccount = 1L;
        String token = "mocked-token";

//
//        TransferDto transferDto = new TransferDto(
//                1L,
//                123L,
//                456L,
//                "Title",
//                LocalDateTime.of(2024, 8, 8, 0, 0),
//                BigDecimal.valueOf(100)
//        );

        mockMvc.perform(post("/accounts/{idAccount}/transfers", idAccount)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idTransfer\": 1, \"sender\": 123, \"recipient\": 456, \"amount\": 100, \"title\": \"Title\", \"date\": \"2024-08-08T00:00:00\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer made successfully"));

        verify(userAuthenticationProvider, times(1)).validateToken("mocked-token");
        verify(transferService, times(1)).processTransfer(eq(idAccount), any(TransferDto.class));
    }



    private String generateJwtTokenForUser(Long idAccount) {
        AuthDto mockAuthDto = new AuthDto(idAccount, userAuthenticationProvider.createToken(idAccount));
        Mockito.when(accountService.findByIdAccount(String.valueOf(idAccount))).thenReturn(mockAuthDto);

        return mockAuthDto.token();
    }


    @Test
    public void testProcessTransfer_SameSenderAndRecipient() {
        // Arrange
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                123L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0),
                BigDecimal.valueOf(100)
        );

        doThrow(new TransferException("Sender and recipient cannot be the same"))
                .when(transferService).processTransfer(idAccount, transferDto);

        // Act
        ResponseEntity<String> response = transferController.processTransfer(idAccount, transferDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sender and recipient cannot be the same", response.getBody());
        verify(transferService).processTransfer(idAccount, transferDto);
    }

    @Test
    public void testProcessTransfer_NegativeAmount() {
        // Arrange
        Long idAccount = 1L;
        TransferDto transferDto = new TransferDto(
                1L,
                123L,
                456L,
                "Title",
                LocalDateTime.of(2024, 8, 8, 0, 0),
                BigDecimal.valueOf(-5)
        );

        doThrow(new TransferException("Transfer amount must be greater than zero"))
                .when(transferService).processTransfer(idAccount, transferDto);

        // Act
        ResponseEntity<String> response = transferController.processTransfer(idAccount, transferDto);


        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Transfer amount must be greater than zero", response.getBody());
        verify(transferService).processTransfer(idAccount, transferDto);
    }
}