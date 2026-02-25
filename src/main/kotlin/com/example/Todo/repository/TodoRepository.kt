// repository/TodoRepository.kt
package com.example.Todo.repository

import com.example.Todo.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository : JpaRepository<Todo, Long> {
    fun findByUserId(userId: Long): List<Todo>
}