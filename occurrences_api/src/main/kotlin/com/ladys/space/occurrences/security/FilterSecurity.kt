package com.ladys.space.occurrences.security

import com.ladys.space.occurrences.constants.ErrorMessageConstants.Companion.MISSING_AUTHORIZATION_HEADER
import com.ladys.space.occurrences.errors.exceptions.InvalidAccessException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FilterSecurity(
        @Qualifier("handlerExceptionResolver")
        private val resolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filter: FilterChain) {
        val token: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (token == null || !token.startsWith("Bearer ")) {
            this.resolver.resolveException(
                    request,
                    response,
                    null,
                    InvalidAccessException(MISSING_AUTHORIZATION_HEADER)
            )
            return
        }
        filter.doFilter(request, response)
    }

}
