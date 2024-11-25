package com.example.bankapp.Transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferDto(
        Long idTransfer,
        @NotNull(message = "Sender is required") Long sender,
        @NotNull(message = "Recipient is required") Long recipient,
        @NotBlank(message = "Title is required") String title,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date,
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount
) {}