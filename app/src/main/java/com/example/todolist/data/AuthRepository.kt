package com.example.todolist.data

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun login(email: String, pass: String): Result<Boolean>
    suspend fun signup(email: String, pass: String): Result<Boolean>
    fun logout()
}