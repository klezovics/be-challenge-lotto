package com.lotto24.backend.domain.transaction.model.entity

import com.lotto24.backend.domain.common.entity.BaseEntity
import com.lotto24.backend.domain.transaction.service.exception.InsufficientBalanceException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID
import kotlin.math.absoluteValue

@Entity
@Table(name = "balance")
data class BalanceEntity(
    @Column(name = "customer_id", nullable = false)
    var customerId: UUID? = null,

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    var customer: CustomerEntity? = null,

    @Column(name = "balance_cents", nullable = false)
    var balanceCents: Long? = null,

    @OneToMany(mappedBy = "balance")
    var transactions: MutableList<TransactionEntity> = mutableListOf()
) : BaseEntity() {

    fun addTransaction(transaction: TransactionEntity) {
        validateNewTransaction(transaction)

        balanceCents = balanceCents?.plus(transaction.amountCents!!)
        transactions.add(transaction)
        transaction.balance = this
    }

    private fun validateNewTransaction(transaction: TransactionEntity) {
        if (transaction.amountCents!! < 0) {
            if (balanceCents!! < transaction.amountCents!!.absoluteValue) {
                throw InsufficientBalanceException(id!!, balanceCents!!, transaction.amountCents!!)
            }
        }
    }

    fun rollbackBy(rollbackAmount: Long) {
        balanceCents = balanceCents!!.minus(rollbackAmount)
    }
}
