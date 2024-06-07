package com.lotto24.backend.domain.transaction.service.exception

import com.lotto24.backend.domain.common.exception.ApiException
import java.util.UUID

class InsufficientBalanceException(
    val balanceId: UUID,
    val currentBalanceCents: Long,
    val transactionAmountCents: Long
) : ApiException(
    message = "Insufficient balance",
    metadata = mapOf(
        "balanceId" to balanceId,
        "currentBalanceCents" to currentBalanceCents,
        "transactionAmountCents" to transactionAmountCents
    )
)
