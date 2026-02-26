package com.example.Todo.service

import com.example.Todo.dto.AuthResponse
import com.example.Todo.dto.LoginRequest
import com.example.Todo.dto.RefreshRequest
import com.example.Todo.dto.RegisterRequest
import com.example.Todo.entity.User
import com.example.Todo.exception.InvalidCredentialsException
import com.example.Todo.exception.UserAlreadyExistsException
import com.example.Todo.exception.UserNotFoundException
import com.example.Todo.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val refreshTokenService: RefreshTokenService
) {

    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.findByUsername(request.username) != null) {
            throw UserAlreadyExistsException("Username '${request.username}' is already taken")
        }
        val user = User(
            username = request.username,
            password = encoder.encode(request.password)!!,
            email = request.email,
            role = request.role
        )
        userRepository.save(user)
        val accessToken = jwtService.generateToken(user)
        val refreshToken = refreshTokenService.createRefreshToken(user)
        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken.token,
            role = user.role.name
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (e: BadCredentialsException) {
            throw InvalidCredentialsException("Invalid username or password")
        }
        val user = userRepository.findByUsername(request.username)
            ?: throw UserNotFoundException("User not found: ${request.username}")
        val accessToken = jwtService.generateToken(user)
        val refreshToken = refreshTokenService.createRefreshToken(user)
        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken.token,
            role = user.role.name
        )
    }

    fun refresh(request: RefreshRequest): AuthResponse {
        val refreshToken = refreshTokenService.findByToken(request.refreshToken)
            ?: throw UserNotFoundException("Refresh token not found")
        refreshTokenService.verifyExpiration(refreshToken)
        val user = refreshToken.user ?: throw UserNotFoundException("User not found")
        val newAccessToken = jwtService.generateToken(user)
        val newRefreshToken = refreshTokenService.createRefreshToken(user)
        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken.token,
            role = user.role.name
        )
    }

    fun logout(username: String) {
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")
        refreshTokenService.deleteByUser(user)
    }
}