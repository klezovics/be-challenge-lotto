package com.lotto24.backend.support.mother

import com.lotto24.backend.domain.transaction.model.entity.BalanceEntity

object BalanceMother {

    fun valid() = BalanceEntity(
        customerId = null,
        balanceCents = 100,
    )
}
