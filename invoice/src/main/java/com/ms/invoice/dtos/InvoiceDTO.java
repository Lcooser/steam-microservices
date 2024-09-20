package com.ms.invoice.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.UUID;

public record InvoiceDTO(
        @NotBlank UUID orderId,
        @NotBlank UUID userId,
        @NotBlank UUID gameId,
        @NotBlank String gameName,
        @NotBlank String companyCnpj,
        @NotBlank String companyName,
        @NotBlank int quantity,
        @NotBlank double amount,
        @NotBlank Date paymentDate,
        @NotBlank String playerCpf,
        @NotBlank String playerName,
        @NotBlank String playerEmail,
        @NotBlank String playerPhone
) {
}
