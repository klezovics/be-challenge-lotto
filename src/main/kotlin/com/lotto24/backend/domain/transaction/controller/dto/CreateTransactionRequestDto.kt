package com.lotto24.backend.domain.transaction.controller.dto

import jakarta.validation.constraints.Min
import java.util.UUID

data class CreateTransactionRequestDto(
    val tenantId: UUID,
    @field:Min(1)
    val customerNumber: Long,
    val amountCents: Long
)
