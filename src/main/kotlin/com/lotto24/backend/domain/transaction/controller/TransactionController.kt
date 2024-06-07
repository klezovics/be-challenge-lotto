package com.lotto24.backend.domain.transaction.controller

import com.lotto24.backend.domain.transaction.controller.TransactionController.Companion.URI_PREFIX
import com.lotto24.backend.domain.transaction.controller.dto.CreateTransactionRequestDto
import com.lotto24.backend.domain.transaction.controller.dto.CreateTransactionResponseDto
import com.lotto24.backend.domain.transaction.controller.dto.GetLatestTransactionsRequestDto
import com.lotto24.backend.domain.transaction.controller.dto.GetLatestTransactionsResponseDto
import com.lotto24.backend.domain.transaction.controller.dto.RollbackTransactionRequestDto
import com.lotto24.backend.domain.transaction.mapper.TransactionMapper
import com.lotto24.backend.domain.transaction.service.TransactionService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(URI_PREFIX)
class TransactionController(
    private val transactionService: TransactionService,
    private val mapper: TransactionMapper
) {
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    fun createTransaction(
        @RequestBody @Valid dto: CreateTransactionRequestDto
    ) = CreateTransactionResponseDto(
        with(dto) { transactionService.createTransaction(tenantId, customerNumber, mapper.toEntity(dto)) }
    )

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getLatestTransactions(
        @RequestBody @Valid dto: GetLatestTransactionsRequestDto
    ) = GetLatestTransactionsResponseDto(
        dto.run {
            transactionService.getLatestTransactions(tenantId, customerNumber, transactionCount)
        }.map {
            mapper.toDto(it)
        }
    )

    @PutMapping("/rollback")
    @PreAuthorize("hasRole('USER')")
    fun rollbackTransaction(
        @RequestBody @Valid dto: RollbackTransactionRequestDto
    ) = transactionService.rollbackTransaction(dto.transactionId)

    companion object {
        const val URI_PREFIX = "/api/transaction"
    }
}
