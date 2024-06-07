package com.lotto24.backend.domain.transaction.repository

import com.lotto24.backend.domain.transaction.model.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface CustomerRepository : JpaRepository<CustomerEntity, UUID> {

    fun findByTenantIdAndNumber(tenantId: UUID, customerNumber: Long): Optional<CustomerEntity>
}
