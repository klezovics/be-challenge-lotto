package com.lotto24.backend.domain.transaction.controller

import com.lotto24.backend.BaseSpringBootTest
import com.lotto24.backend.domain.transaction.controller.dto.*
import com.lotto24.backend.domain.transaction.service.TransactionService
import com.lotto24.backend.support.mother.TransactionMother
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class TransactionControllerTest : BaseSpringBootTest() {

    @MockBean
    private lateinit var transactionService: TransactionService

    @Test
    fun `given a request to create a new transaction, then can create it`() {
        val requestDto = CreateTransactionRequestDto(
            tenantId = UUID.randomUUID(),
            customerNumber = 1L,
            amountCents = 100L
        )

        val createdTransactionId = UUID.randomUUID().also {
            whenever(transactionService.createTransaction(any(), any(), any())).thenReturn(it)
        }

        val responseDto = mockMvc.perform(
            post("${TransactionController.URI_PREFIX}/create")
                .with(user("customer1").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString.let { objectMapper.readValue(it, CreateTransactionResponseDto::class.java) }

        assertThat(responseDto.transactionId).isEqualTo(createdTransactionId)
    }

    @Test
    fun `given a request to get the latest transactions, then can do it`() {
        val transactions = listOf(
            TransactionMother.valid().apply { id = UUID.randomUUID() },
            TransactionMother.valid().apply { id = UUID.randomUUID() }
        )
        val requestDto = GetLatestTransactionsRequestDto(
            tenantId = UUID.randomUUID(),
            customerNumber = 1L,
            transactionCount = 2
        ).also {
            whenever(transactionService.getLatestTransactions(eq(it.tenantId), eq(it.customerNumber), eq(it.transactionCount))).thenReturn(
                transactions
            )
        }

        val responseDto = mockMvc.perform(
            post(TransactionController.URI_PREFIX)
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString.let { objectMapper.readValue(it, GetLatestTransactionsResponseDto::class.java) }

        assertThat(responseDto.transactions.size).isEqualTo(2)
    }

    @Test
    fun `given a request to rollback a transaction then can do it`() {
        val requestDto = RollbackTransactionRequestDto(UUID.randomUUID())

        mockMvc.perform(
            put("${TransactionController.URI_PREFIX}/rollback")
                .with(user("customer1").roles("USER").authorities())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
            .andExpect(status().isOk)
    }
}
