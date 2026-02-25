package com.example.Todo.service

import com.example.Todo.entity.RefreshToken
import com.example.Todo.entity.User
import com.example.Todo.repository.RefreshTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository
) {
    // Refresh token valid for 7 days
    private val refreshTokenDuration = 7L * 24 * 60 * 60 * 1000

    @Transactional
    fun createRefreshToken(user: User): RefreshToken {
        refreshTokenRepository.deleteByUser(user)

        val refreshToken = RefreshToken(
            token = UUID.randomUUID().toString(),
            user = user,
            expiryDate = Instant.now().plusMillis(refreshTokenDuration)
        )
        return refreshTokenRepository.save(refreshToken)
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expiryDate.isBefore(Instant.now())) {
            refreshTokenRepository.delete(token)
            throw RuntimeException("Refresh token expired. Please login again.")
        }
        return token
    }

    fun findByToken(token: String): RefreshToken? {
        return refreshTokenRepository.findByToken(token)
    }

    @Transactional
    fun deleteByUser(user: User) {
        refreshTokenRepository.deleteByUser(user)
    }
}