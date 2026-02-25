package com.example.Todo.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    private val secretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    // Called after login — creates a token for the user
    fun generateToken(userDetails: UserDetails): String {
        return Jwts.builder()
            .setSubject(userDetails.username)   // stores username inside token
            .setIssuedAt(Date())                // when token was created
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // expires in 24hrs
            .signWith(secretKey)                // signs with secret key
            .compact()                          // builds the final token string
    }

    // Pulls the username out of the token
    fun extractUsername(token: String): String {
        return getClaims(token).getSubject()
    }

    // Checks token belongs to this user and hasn't expired
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return getClaims(token).expiration.before(Date())
    }

    // Decodes the token and returns its contents
    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}