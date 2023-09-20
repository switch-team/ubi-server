package dev.jombi.ubi

import dev.jombi.ubi.util.jwt.TokenFactory
import dev.jombi.ubi.util.jwt.TokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val tokenFactory: TokenFactory) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .anyRequest().authenticated()
                    .requestMatchers("admin/**").hasAnyAuthority("ADMIN")
                    .requestMatchers("auth/**").permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(TokenFilter(tokenFactory), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}