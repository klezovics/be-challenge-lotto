package com.lotto24.backend.config.security

import com.lotto24.backend.config.security.exception.ApiSecurityException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SecurityManager {

    private fun getCurrentUser(): ApiUser {
        val authentication = SecurityContextHolder.getContext().authentication!!
        return if (authentication.principal is ApiUser) {
            authentication.principal as ApiUser
        } else throw IllegalStateException("Principal is not of type ApiUser")
    }

    fun checkCanAccessCustomerData(tenantId: UUID, customerNumber: Long) {
        getCurrentUser().run {
            if (tenantId != this.tenantId || customerNumber != this.customerNumber) {
                throw ApiSecurityException.accessDenied(tenantId, customerNumber, this)
            }
        }
    }
}
