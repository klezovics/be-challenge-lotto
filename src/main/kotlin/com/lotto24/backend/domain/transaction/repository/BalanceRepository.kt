package com.lotto24.backend.domain.transaction.repository

import com.lotto24.backend.domain.transaction.model.entity.BalanceEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface BalanceRepository : JpaRepository<BalanceEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    // Base method override on purpose, to avoid accidental use of its analogue without a lock
    override fun findById(id: UUID): Optional<BalanceEntity>
}
