package com.lotto24.backend.domain.transaction.mapper

import com.lotto24.backend.domain.transaction.controller.dto.CreateTransactionRequestDto
import com.lotto24.backend.domain.transaction.controller.dto.TransactionDto
import com.lotto24.backend.domain.transaction.model.entity.TransactionEntity
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface TransactionMapper {
    fun toEntity(createTransactionRequestDto: CreateTransactionRequestDto): TransactionEntity
    fun toDto(transactionEntity: TransactionEntity): TransactionDto
}
