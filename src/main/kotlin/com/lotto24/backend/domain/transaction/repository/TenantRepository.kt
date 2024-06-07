package com.lotto24.backend.domain.transaction.repository

import com.lotto24.backend.domain.transaction.model.entity.TenantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TenantRepository : JpaRepository<TenantEntity, UUID>
