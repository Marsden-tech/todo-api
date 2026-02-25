package com.example.Todo.dto

import com.example.Todo.entity.Role

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val role: Role = Role.USER
)