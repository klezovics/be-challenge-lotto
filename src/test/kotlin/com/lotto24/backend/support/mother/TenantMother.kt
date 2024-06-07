package com.lotto24.backend.support.mother

import com.lotto24.backend.domain.transaction.model.entity.TenantEntity

object TenantMother {

    fun valid() = TenantEntity(
        name = "test-tenant",
    )
}
