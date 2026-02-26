package com.example.Todo.controller

import com.example.Todo.dto.TodoRequest
import com.example.Todo.dto.TodoResponse
import com.example.Todo.service.TodoService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService
) {

    @GetMapping
    fun getAll(): ResponseEntity<List<TodoResponse>> =
        ResponseEntity.ok(todoService.getAllTodos())

    @PostMapping
    fun create(@Valid @RequestBody request: TodoRequest): ResponseEntity<TodoResponse> =
        ResponseEntity.ok(todoService.createTodo(request))

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: TodoRequest): ResponseEntity<TodoResponse> =
        ResponseEntity.ok(todoService.updateTodo(id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        todoService.deleteTodo(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/complete")
    fun complete(@PathVariable id: Long): ResponseEntity<TodoResponse> =
        ResponseEntity.ok(todoService.completeTodo(id))
}