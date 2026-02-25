package com.example.Todo.dto

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val role: String
)