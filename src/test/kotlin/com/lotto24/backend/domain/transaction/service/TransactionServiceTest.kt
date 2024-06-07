package com.lotto24.backend.domain.transaction.service

import com.lotto24.backend.BaseSpringBootTest
import com.lotto24.backend.config.security.SecurityManager
import com.lotto24.backend.domain.transaction.model.entity.TransactionStatus
import com.lotto24.backend.domain.transaction.repository.BalanceRepository
import com.lotto24.backend.domain.transaction.repository.TransactionRepository
import com.lotto24.backend.support.mother.TransactionMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

class TransactionServiceTest : BaseSpringBootTest() {

    @Autowired lateinit var transactionService: TransactionService
    @Autowired lateinit var transactionRepository: TransactionRepository
    @Autowired lateinit var balanceRepository: BalanceRepository
    @MockBean lateinit var securityManager: SecurityManager

    @BeforeEach
    fun setup() {
        doNothing().whenever(securityManager).checkCanAccessCustomerData(any(), any())
    }

    @Test
    @Transactional
    fun `given a request to create a customer transaction then can do it`() {
        val balanceId = persistedBalanceFactory.build().id!!
        val balance = balanceRepository.findById(balanceId).get()
        val startingBalanceCents = balance.balanceCents!!

        val transaction = TransactionMother.valid()

        val transactionId = transactionService.createTransaction(balance.customer!!.tenantId!!, balance.customer!!.number!!, transaction)

        val createdTransaction = transactionRepository.findById(transactionId).get()
        createdTransaction.run {
            assertThat(status).isEqualTo(TransactionStatus.VALID)
            assertThat(amountCents).isEqualTo(transaction.amountCents)
            assertThat(this.balance!!.id).isEqualTo(balance.id!!)
        }

        val updatedBalance = balanceRepository.findById(balanceId).get()
        updatedBalance.run {
            assertThat(balanceCents).isEqualTo(startingBalanceCents + transaction.amountCents!!)
        }
    }

    @Test
    @Transactional
    fun `given a request to get the latest N transactions, then can do it`() {
        // Setup test data
        val balance = persistedBalanceFactory.build().id!!.let { balanceRepository.findById(it).get() }
        val customer = balance.customer!!
        val tenantId = customer.tenantId!!
        val customerNumber = customer.number!!

        val otherCustomerBalance = persistedBalanceFactory.build().id!!.let { balanceRepository.findById(it).get() }

        val now = Instant.now()
        val transactions = listOf(
            TransactionMother.valid(balance = balance, createdAt = now.minusSeconds(900)), // recent. will be returned
            TransactionMother.valid(balance = balance, createdAt = now.minusSeconds(1800)), // recent. will be returned
            TransactionMother.valid(balance = balance, createdAt = now.minusSeconds(3600)), // too old. won't be returned
            TransactionMother.valid(balance = balance, createdAt = now.minusSeconds(3600)), // too old. won't be returned
            TransactionMother.valid(balance = otherCustomerBalance, createdAt = now.minusSeconds(100)), // wrong customer. won't be returned
            TransactionMother.valid(balance = otherCustomerBalance, createdAt = now.minusSeconds(100)) // wrong customer. won't be returned
        ).map { transactionRepository.save(it) }
        val expectedTransactionIds = transactions.take(2).map { it.id!! }

        // Fetch 2 most recent transactions
        val latestTransactions = transactionService.getLatestTransactions(tenantId, customerNumber, 2)

        // Verify 2 most recent transactions are returned
        assertThat(latestTransactions.size).isEqualTo(2)
        assertThat(latestTransactions.map { it.id }).containsExactlyElementsOf(expectedTransactionIds)
    }

    @Test
    @Transactional
    fun `given a request to rollback a transaction then can do it`() {
        // Setup test data
        val balanceId = persistedBalanceFactory.build().id!!
        val balance = balanceRepository.findById(balanceId).get()
        val startingBalanceCents = balance.balanceCents

        val transaction = TransactionMother.valid()
        val transactionId = transactionService.createTransaction(balance.customer!!.tenantId!!, balance.customer!!.number!!, transaction)

        // Rollback transaction
        transactionService.rollbackTransaction(transactionId)

        // Verify transaction entity is correctly updated as a result of rollback
        val rolledbackTransaction = transactionRepository.findById(transactionId).get()
        rolledbackTransaction.run {
            assertThat(status).isEqualTo(TransactionStatus.ROLLED_BACK)
        }

        // Verify balance entity is correctly updated as a result of rollback
        val updatedBalance = balanceRepository.findById(balanceId).get()
        updatedBalance.run {
            assertThat(balanceCents).isEqualTo(startingBalanceCents)
        }
    }
}
