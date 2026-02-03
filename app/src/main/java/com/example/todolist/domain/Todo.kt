package com.example.todolist.domain

data class Todo(
    val id: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,

    )


//Fake Objects
val todo1 = Todo(
    id = 1,
    title = "Teste 1",
    description = "Colocar lixo para fora",
    isCompleted = false,
)

val todo2 = Todo(
    id = 2,
    title = "Teste 2",
    description = "Lavar a louça",
    isCompleted = true,
)

val todo3 = Todo(
    id = 3,
    title = "Teste 3",
    description = "Lavar a roupa",
    isCompleted = false,
)




