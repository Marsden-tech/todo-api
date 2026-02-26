package com.example.Todo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class TodoRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    val title: String
)