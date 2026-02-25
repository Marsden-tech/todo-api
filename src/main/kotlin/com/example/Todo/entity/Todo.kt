package com.example.Todo.entity

import jakarta.persistence.*

@Entity
@Table(name = "todos")
class Todo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String = "",

    @Column(nullable = false)
    var completed: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)