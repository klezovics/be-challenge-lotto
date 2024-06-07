package com.lotto24.backend.config.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID
data class ApiUser(
    val userName: String,
    val pwd: String,
    val auths: MutableCollection<out GrantedAuthority> = mutableListOf(),
    val customerNumber: Long? = null,
    val tenantId: UUID? = null
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = auths
    override fun getPassword(): String = pwd
    override fun getUsername(): String = userName
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
