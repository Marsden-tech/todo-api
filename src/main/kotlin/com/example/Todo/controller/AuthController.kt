package com.example.Todo.controller

import com.example.Todo.dto.AuthResponse
import com.example.Todo.dto.LoginRequest
import com.example.Todo.dto.RefreshRequest
import com.example.Todo.dto.RegisterRequest
import com.example.Todo.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.refresh(request))
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        val username = SecurityContextHolder.getContext().authentication!!.name
        authService.logout(username)
        return ResponseEntity.noContent().build()
    }
}