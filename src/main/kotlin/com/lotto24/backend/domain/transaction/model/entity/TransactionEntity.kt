package com.lotto24.backend.domain.transaction.model.entity

import com.lotto24.backend.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SourceType
import java.time.Instant

@Entity
@Table(name = "transaction")
class TransactionEntity(
    @ManyToOne
    @JoinColumn(name = "balance_id", referencedColumnName = "id")
    var balance: BalanceEntity? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: TransactionStatus = TransactionStatus.VALID,

    @Column(name = "amount_cents", nullable = false)
    var amountCents: Long? = null,

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant? = null
) : BaseEntity() {
    fun rollback() {
        status = TransactionStatus.ROLLED_BACK
    }
}

enum class TransactionStatus {
    VALID, ROLLED_BACK
}
