package dev.jombi.ubi.config

import dev.jombi.ubi.filter.TokenFilter
import dev.jombi.ubi.util.jwt.TokenFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
class SecurityConfig(private val tokenFactory: TokenFactory) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .formLogin { it.disable() }
            .csrf { it.ignoringRequestMatchers(AntPathRequestMatcher("/**")) }
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
            .addFilterBefore(TokenFilter(tokenFactory), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}