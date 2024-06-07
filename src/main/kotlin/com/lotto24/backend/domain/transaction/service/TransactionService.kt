package com.lotto24.backend.domain.transaction.service

import com.lotto24.backend.config.security.SecurityManager
import com.lotto24.backend.domain.common.exception.EntityNotFoundException
import com.lotto24.backend.domain.transaction.model.entity.BalanceEntity
import com.lotto24.backend.domain.transaction.model.entity.TransactionEntity
import com.lotto24.backend.domain.transaction.repository.BalanceRepository
import com.lotto24.backend.domain.transaction.repository.CustomerRepository
import com.lotto24.backend.domain.transaction.repository.TransactionRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionService(
    private val customerRepository: CustomerRepository,
    private val transactionRepository: TransactionRepository,
    private val balanceRepository: BalanceRepository,
    private val securityManager: SecurityManager
) {
    @Transactional
    fun createTransaction(
        tenantId: UUID,
        customerNumber: Long,
        transaction: TransactionEntity
    ): UUID {
        securityManager.checkCanAccessCustomerData(tenantId, customerNumber)

        val balanceId = customerRepository.findByTenantIdAndNumber(tenantId, customerNumber)
            .orElseThrow { throw EntityNotFoundException.customerByTenantIdAndNumber(tenantId, customerNumber) }
            .balance!!.id!!

        // The balance entity is read out by id to ensure that it's locked before balance update
        balanceRepository.findById(balanceId)
            .orElseThrow { throw EntityNotFoundException(balanceId, BalanceEntity::class) }
            .apply {
                addTransaction(transaction)
            }

        return transactionRepository.save(transaction).id!!
    }

    fun getLatestTransactions(tenantId: UUID, customerNumber: Long, transactionCount: Int): List<TransactionEntity> = transactionRepository.findLatestTransactions(
        tenantId,
        customerNumber,
        PageRequest.of(0, transactionCount)
    )

    @Transactional
    fun rollbackTransaction(transactionId: UUID) {
        val transaction = transactionRepository.findById(transactionId).orElseThrow {
            throw EntityNotFoundException(transactionId, TransactionEntity::class)
        }.also {
            securityManager.checkCanAccessCustomerData(it.balance!!.customer!!.tenantId!!, it.balance!!.customer!!.number!!)
        }

        transaction.rollback()

        // The balance entity is read out by id to ensure that it's locked before balance update
        balanceRepository.findById(transaction.balance!!.id!!).orElseThrow {
            throw EntityNotFoundException(transaction.balance!!.id!!, BalanceEntity::class)
        }.apply {
            rollbackBy(transaction.amountCents!!)
        }
    }
}
