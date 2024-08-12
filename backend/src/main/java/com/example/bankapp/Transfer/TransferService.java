package com.example.bankapp.Transfer;

import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.TransferMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final AccountService accountService;

    public TransferService(TransferRepository transferRepository, AccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    public Optional<Transfer> getTransferById(Long idTransfer) {
        return transferRepository.findById(idTransfer);
    }

    public List<TransferDto> getTransfersByAccount(Long idAccount) {
        return transferRepository.findByAccount_IdAccountOrderByDateDesc(idAccount)
                .stream()
                .map(TransferMapper.INSTANCE::toDto)
                .toList();
    }

    public List<TransferDto> getTransfersBySender(Long idAccount) {
        return transferRepository.findBySenderAndAccount_IdAccount(
                        accountService.getNumberByIdAccount(idAccount),
                        idAccount
                )
                .stream()
                .map(TransferMapper.INSTANCE::toDto)
                .toList();
    }

    public List<TransferDto> getTransfersByRecipient(Long idAccount) {
        return transferRepository.findByRecipientAndAccount_IdAccount(
                        accountService.getNumberByIdAccount(idAccount),
                        idAccount)
                .stream()
                .map(TransferMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

    }

    public void makeTransfer(Long idAccount, TransferDto transferDTO) {
        Transfer senderTransfer = TransferMapper.INSTANCE.toEntity(transferDTO);
        senderTransfer.setAccount(
                accountService.getAccountById(idAccount)
        );


        Transfer recipientTransfer = TransferMapper.INSTANCE.toEntity(transferDTO);
        recipientTransfer.setAccount(
                accountService.getAccountById(
                        accountService.getIdAccountByNumber(transferDTO.recipient())
                )
        );

        transferRepository.save(senderTransfer);
        transferRepository.save(recipientTransfer);

        accountService.transferFunds(senderTransfer.getAccount().getIdAccount(), recipientTransfer.getAccount().getIdAccount(), senderTransfer.getAmount());
    }


}
