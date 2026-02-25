package com.example.Todo.config

import com.example.Todo.service.JwtService
import com.example.Todo.service.UserDetailsServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Step 1 - Get the Authorization header
        val authHeader = request.getHeader("Authorization")

        // Step 2 - If no token, skip and continue
        // This handles public endpoints like /api/auth/register
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // Step 3 - Strip "Bearer " prefix to get raw token
        val token = authHeader.substring(7)

        // Step 4 - Extract username from token
        val username = jwtService.extractUsername(token)

        // Step 5 - Only authenticate if not already authenticated
        if (SecurityContextHolder.getContext().authentication == null) {
            val user = userDetailsService.loadUserByUsername(username)

            // Step 6 - Validate token
            if (jwtService.isTokenValid(token, user)) {

                // Step 7 - Create authentication object and put it in SecurityContext
                val authToken = UsernamePasswordAuthenticationToken(
                    user, null, user.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        // Step 8 - Continue to the next filter or controller
        filterChain.doFilter(request, response)
    }
}