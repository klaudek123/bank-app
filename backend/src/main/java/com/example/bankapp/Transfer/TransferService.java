package com.example.bankapp.Transfer;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountService;
//import com.example.bankapp.Kafka.KafkaProducer;
//import com.example.bankapp.Kafka.TransferEvent;
import com.example.bankapp.Mappers.TransferMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final AccountService accountService;
//    private final KafkaProducer kafkaProducer;
    private final TransferMapper transferMapper;

    public TransferService(TransferRepository transferRepository, AccountService accountService, TransferMapper transferMapper) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
//        this.kafkaProducer = kafkaProducer;
        this.transferMapper = transferMapper;
    }

    public Optional<Transfer> getTransferById(Long idTransfer) {
        return transferRepository.findById(idTransfer);
    }

    public List<TransferDto> getTransfersByAccount(Long idAccount) {
        return transferRepository.findByAccount_IdAccountOrderByDateDesc(idAccount)
                .stream()
                .map(transferMapper::toDto)
                .toList();
    }

    public List<TransferDto> getTransfersBySender(Long idAccount) {
        return transferRepository.findBySenderAndAccount_IdAccount(
                        accountService.getNumberByIdAccount(idAccount),
                        idAccount
                )
                .stream()
                .map(transferMapper::toDto)
                .toList();
    }

    public List<TransferDto> getTransfersByRecipient(Long idAccount) {
        return transferRepository.findByRecipientAndAccount_IdAccount(
                        accountService.getNumberByIdAccount(idAccount),
                        idAccount)
                .stream()
                .map(transferMapper::toDto)
                .collect(Collectors.toList());

    }

    public void processTransfer(Long idAccount, TransferDto transferDTO) {
        Account senderAccount = accountService.getAccountById(idAccount);
        Account recipientAccount = accountService.getAccountByNumber(transferDTO.recipient());

        validateTransfer(senderAccount, recipientAccount, transferDTO.amount());

        Transfer senderTransfer = createTransferEntity(senderAccount, transferDTO);
        Transfer recipientTransfer = createTransferEntity(recipientAccount, transferDTO);

        transferRepository.save(senderTransfer);
        transferRepository.save(recipientTransfer);

        accountService.transferFunds(senderTransfer.getAccount().getIdAccount(), recipientTransfer.getAccount().getIdAccount(), senderTransfer.getAmount());

//        kafkaProducer.sendTransferEvent(createTransferEvent(senderTransfer, recipientTransfer));
    }

    private void validateTransfer(Account sender, Account recipient, BigDecimal amount) {
        if (sender.equals(recipient)) {
            throw new TransferException("Sender and recipient cannot be the same");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferException("Transfer amount must be greater than zero");
        }
    }

    private Transfer createTransferEntity(Account account, TransferDto dto) {
        Transfer transfer = transferMapper.toEntity(dto);
        transfer.setAccount(account);
        return transfer;
    }

//    private TransferEvent createTransferEvent(Transfer senderTransfer, Transfer recipientTransfer) {
//        return new TransferEvent(
//                senderTransfer.getSender(),
//                recipientTransfer.getRecipient(),
//                senderTransfer.getAmount(),
//                senderTransfer.getTitle(),
//                senderTransfer.getDate()
//        );
//    }


}
