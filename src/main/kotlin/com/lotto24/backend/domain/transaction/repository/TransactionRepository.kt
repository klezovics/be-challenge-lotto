package com.lotto24.backend.domain.transaction.repository

import com.lotto24.backend.domain.transaction.model.entity.TransactionEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TransactionRepository : JpaRepository<TransactionEntity, UUID> {

    @Query(
        """
            SELECT t FROM TransactionEntity t JOIN t.balance b JOIN b.customer c 
            WHERE c.tenantId = :tenantId AND c.number = :customerNumber 
            ORDER BY t.createdAt DESC
        """
    )
    fun findLatestTransactions(
        tenantId: UUID,
        customerNumber: Long,
        pageable: Pageable
    ): List<TransactionEntity>
}
