package com.example.Todo.repository

import com.example.Todo.entity.RefreshToken
import com.example.Todo.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): RefreshToken?
    fun deleteByUser(user: User)
}