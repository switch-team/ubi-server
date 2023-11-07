package dev.jombi.ubi.service

import dev.jombi.ubi.repository.UserRepository
import dev.jombi.ubi.util.UUIDSafe
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(uuid: String): UserDetails {
        val user = userRepository.findUserById(UUIDSafe(uuid))
            ?: throw UsernameNotFoundException("User not found.")

        return User(
            "${user.id}",
            user.password,
            user.authorities.map { SimpleGrantedAuthority(it.authorityName) })
    }
}