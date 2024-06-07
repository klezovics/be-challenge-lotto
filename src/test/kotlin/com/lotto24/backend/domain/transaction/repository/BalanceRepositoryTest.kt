package com.lotto24.backend.domain.transaction.repository

import com.lotto24.backend.BaseSpringBootTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

class BalanceRepositoryTest : BaseSpringBootTest() {

    @Autowired lateinit var balanceRepository: BalanceRepository
    @Test
    @Transactional(readOnly = false)
    fun `given a balance id, then can find balance entity by id`() {
        val balance = persistedBalanceFactory.build()

        val balanceFromDb = balanceRepository.findById(balance.id!!).get()
        balanceFromDb.run {
            assertThat(id!!).isEqualTo(balance.id)
            assertThat(balanceCents).isEqualTo(balance.balanceCents)
        }
    }
}
