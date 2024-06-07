package com.lotto24.backend.support.factory

import com.lotto24.backend.domain.transaction.model.entity.BalanceEntity
import com.lotto24.backend.domain.transaction.repository.BalanceRepository
import com.lotto24.backend.domain.transaction.repository.CustomerRepository
import com.lotto24.backend.domain.transaction.repository.TenantRepository
import com.lotto24.backend.support.mother.BalanceMother
import com.lotto24.backend.support.mother.CustomerMother
import com.lotto24.backend.support.mother.TenantMother
import jakarta.persistence.EntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

// Build persisted data for JUnit tests
open class PersistedBalanceFactory(
    private val balanceRepository: BalanceRepository,
    private val customerRepository: CustomerRepository,
    private val tenantRepository: TenantRepository,
    private val entityManager: EntityManager
) {
    open fun build(): BalanceEntity {
        val tenant = TenantMother.valid().let { tenantRepository.save(it) }
        val customer = CustomerMother.valid().let {
            it.tenantId = tenant.id
            customerRepository.save(it)
        }
        val balance = BalanceMother.valid().let {
            it.customerId = customer.id
            balanceRepository.save(it)
        }

        entityManager.flush()
        entityManager.clear()

        return balance
    }
}

@TestConfiguration
class BalanceFactoryConfig(
    private val balanceRepository: BalanceRepository,
    private val customerRepository: CustomerRepository,
    private val tenantRepository: TenantRepository,
    private val entityManager: EntityManager
) {
    @Bean
    fun balanceFactory(): PersistedBalanceFactory {
        return PersistedBalanceFactory(
            balanceRepository,
            customerRepository,
            tenantRepository,
            entityManager
        )
    }
}
