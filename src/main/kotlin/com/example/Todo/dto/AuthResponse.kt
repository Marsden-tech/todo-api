package com.example.Todo.dto

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val role: String
)