package dev.jombi.ubi.filter

import com.fasterxml.jackson.databind.ObjectMapper
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {
    private val mapper = ObjectMapper()
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: CustomError) {
            response.status = e.reason.status.value()
            response.addHeader("Content-Type", "application/json")
            response.writer.write(mapper.writeValueAsString(GuidedResponseBuilder(e.reason, e.reason.message).noData()))
            return
        }
    }
}