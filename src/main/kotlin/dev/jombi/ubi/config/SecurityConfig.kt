package dev.jombi.ubi.config

import dev.jombi.ubi.filter.ExceptionHandlerFilter
import dev.jombi.ubi.filter.TokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(private val tokenFilter: TokenFilter, private val ehFilter: ExceptionHandlerFilter) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("admin/**").hasAnyAuthority("ADMIN")
                    .requestMatchers("auth/**").permitAll()
                    .requestMatchers("friend/find").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(ehFilter, LogoutFilter::class.java)
            .build()
    }
}