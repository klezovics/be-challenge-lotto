package com.lotto24.backend.domain.transaction.controller.dto

import java.util.UUID

data class GetLatestTransactionsResponseDto(
    val transactions: List<TransactionDto>
)

data class TransactionDto(
    val id: UUID,
    val status: String,
    val amountCents: Long,
)
