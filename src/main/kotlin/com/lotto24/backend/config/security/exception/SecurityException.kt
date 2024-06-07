package com.lotto24.backend.config.security.exception

import com.lotto24.backend.config.security.ApiUser
import com.lotto24.backend.domain.common.exception.ApiException
import java.util.UUID

class ApiSecurityException private constructor(
    msg: String,
    metadata: Map<String, Any> = emptyMap()
) : ApiException(
    message = msg,
    metadata = metadata
) {
    companion object {
        fun accessDenied(tenantId: UUID, customerNumber: Long, currentUser: ApiUser) = ApiSecurityException(
            msg = "Access denied to customer with tenantId $tenantId and customerNumber $customerNumber",
            metadata = mapOf(
                "tenantId" to tenantId,
                "customerNumber" to customerNumber,
                "currentUser" to currentUser
            )
        )
    }
}
