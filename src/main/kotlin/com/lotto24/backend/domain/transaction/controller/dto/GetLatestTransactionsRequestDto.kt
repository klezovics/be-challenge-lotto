package com.lotto24.backend.domain.transaction.controller.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.util.UUID

data class GetLatestTransactionsRequestDto(
    val tenantId: UUID,
    @field:Min(1)
    val customerNumber: Long,
    @field:Min(1)
    @field:Max(100) // limit added for security reasons and to prevent a DDoS attack if credentials compromised
    val transactionCount: Int
)
