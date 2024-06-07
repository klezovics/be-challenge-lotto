package com.lotto24.backend.support.mother

import com.lotto24.backend.domain.transaction.model.entity.CustomerEntity

object CustomerMother {

    fun valid() = CustomerEntity(
        number = 1L,
        tenantId = java.util.UUID.randomUUID()
    )
}
