package com.example.Todo.controller

import com.example.Todo.dto.TodoResponse
import com.example.Todo.dto.UserResponse
import com.example.Todo.repository.TodoRepository
import com.example.Todo.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val userRepository: UserRepository,
    private val todoRepository: TodoRepository
) {

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userRepository.findAll().map {
            UserResponse(id = it.id, username = it.username, email = it.email, role = it.role.name)
        }
        return ResponseEntity.ok(users)
    }

    @GetMapping("/todos")
    fun getAllTodos(): ResponseEntity<List<TodoResponse>> {
        val todos = todoRepository.findAll().map {
            TodoResponse(id = it.id, title = it.title, completed = it.completed)
        }
        return ResponseEntity.ok(todos)
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/todos/{id}")
    fun deleteTodo(@PathVariable id: Long): ResponseEntity<Void> {
        todoRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}