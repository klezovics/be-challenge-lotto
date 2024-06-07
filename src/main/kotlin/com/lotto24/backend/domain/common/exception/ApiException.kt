package com.lotto24.backend.domain.common.exception

abstract class ApiException(
    message: String,
    val metadata: Map<String, Any> = mapOf()
) : RuntimeException(message)
