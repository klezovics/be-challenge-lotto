package com.lotto24.backend.support.mother

import com.lotto24.backend.domain.transaction.model.entity.BalanceEntity
import com.lotto24.backend.domain.transaction.model.entity.TransactionEntity
import com.lotto24.backend.domain.transaction.model.entity.TransactionStatus
import java.time.Instant

object TransactionMother {

    fun valid(
        balance: BalanceEntity? = null,
        createdAt: Instant = Instant.now(),
    ) = TransactionEntity(
        amountCents = 1000L,
        balance = balance,
        status = TransactionStatus.VALID,
        createdAt = createdAt,
    )
}
