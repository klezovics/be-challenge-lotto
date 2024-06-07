package com.lotto24.backend.domain.transaction.model.entity

import com.lotto24.backend.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "tenant")
class TenantEntity(
    @Column(name = "name", nullable = false)
    var name: String? = null
) : BaseEntity()
