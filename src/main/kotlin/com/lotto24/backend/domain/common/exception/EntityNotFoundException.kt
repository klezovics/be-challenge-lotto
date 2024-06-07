package com.lotto24.backend.domain.common.exception

import com.lotto24.backend.domain.transaction.model.entity.BalanceEntity
import java.util.UUID
import kotlin.reflect.KClass

class EntityNotFoundException(
    val id: Any,
    entityClass: KClass<*>,
    metadata: Map<String, Any> = mapOf("id" to id, "entity" to entityClass::simpleName)
) : ApiException(
    message = "Entity ${entityClass.simpleName} with id $id not found",
    metadata = metadata
) {

    companion object {
        fun balance(id: UUID) = EntityNotFoundException(id, BalanceEntity::class)
        fun customerByTenantIdAndNumber(tenantId: UUID, customerNumber: Long) = EntityNotFoundException(
            id = mapOf("customerNumber" to customerNumber, "tenantId" to tenantId),
            entityClass = BalanceEntity::class,
            metadata = mapOf("customerNumber" to customerNumber, "tenantId" to tenantId)
        )
    }
}
