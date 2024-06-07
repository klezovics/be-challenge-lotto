package com.lotto24.backend.config.security

import org.springframework.context.annotation.Bean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomUserDetailsService : UserDetailsService {

    // Hardcoded in-memory users for testing
    override fun loadUserByUsername(username: String): UserDetails {
        if (username == "customer1") {
            val encodedPassword = passwordEncoder().encode("password-customer1")
            return ApiUser(
                userName = "customer1",
                pwd = encodedPassword,
                auths = mutableListOf(SimpleGrantedAuthority("ROLE_USER")),
                customerNumber = 1L, // Add customer number here
                tenantId = UUID.fromString("a3e1b4c0-37b2-4c6f-a232-3f0f8e637e39")
            )
        } else if (username == "admin") {
            val encodedPassword = passwordEncoder().encode("password-admin")
            return ApiUser(
                userName = "admin",
                pwd = encodedPassword,
                auths = mutableListOf(SimpleGrantedAuthority("ROLE_ADMIN")),
                customerNumber = null,
                tenantId = null
            )
        } else {
            throw UsernameNotFoundException("User $username not found")
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
