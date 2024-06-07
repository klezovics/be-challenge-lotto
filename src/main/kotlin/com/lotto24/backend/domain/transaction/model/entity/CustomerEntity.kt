package com.lotto24.backend.domain.transaction.model.entity

import com.lotto24.backend.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "customer")
data class CustomerEntity(
    @Column(name = "number", nullable = false)
    var number: Long? = null,

    @Column(name = "tenant_id", nullable = false)
    var tenantId: UUID? = null,

    @OneToOne(mappedBy = "customer")
    var balance: BalanceEntity? = null
) : BaseEntity()
