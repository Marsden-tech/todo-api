package com.example.Todo.service

import com.example.Todo.dto.TodoRequest
import com.example.Todo.dto.TodoResponse
import com.example.Todo.entity.Todo
import com.example.Todo.repository.TodoRepository
import com.example.Todo.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class TodoService(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) {

    private fun getCurrentUser() =
        userRepository.findByUsername(
            SecurityContextHolder.getContext().authentication!!.name
        ) ?: throw RuntimeException("User not found")

    fun getAllTodos(): List<TodoResponse> {
        val user = getCurrentUser()
        return todoRepository.findByUserId(user.id).map { it.toResponse() }
    }

    fun createTodo(request: TodoRequest): TodoResponse {
        val user = getCurrentUser()
        val todo = Todo(
            title = request.title,
            user = user
        )
        return todoRepository.save(todo).toResponse()
    }

    fun updateTodo(id: Long, request: TodoRequest): TodoResponse {
        val user = getCurrentUser()
        val todo = todoRepository.findById(id).orElseThrow { RuntimeException("Todo not found") }
        if (todo.user.id != user.id) throw RuntimeException("Not authorized")
        // need to replace since title is val, not var
        val updated = Todo(id = todo.id, title = request.title, completed = todo.completed, user = user)
        return todoRepository.save(updated).toResponse()
    }

    fun deleteTodo(id: Long) {
        val user = getCurrentUser()
        val todo = todoRepository.findById(id).orElseThrow { RuntimeException("Todo not found") }
        if (todo.user.id != user.id) throw RuntimeException("Not authorized")
        todoRepository.delete(todo)
    }

    fun completeTodo(id: Long): TodoResponse {
        val user = getCurrentUser()
        val todo = todoRepository.findById(id).orElseThrow { RuntimeException("Todo not found") }
        if (todo.user.id != user.id) throw RuntimeException("Not authorized")
        todo.completed = true
        return todoRepository.save(todo).toResponse()
    }

    private fun Todo.toResponse() = TodoResponse(
        id = id,
        title = title,
        completed = completed
    )
}