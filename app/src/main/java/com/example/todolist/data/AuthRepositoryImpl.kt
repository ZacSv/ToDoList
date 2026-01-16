package com.example.todolist.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun login(email: String, pass: String): Result<Boolean> {
        // Transformamos o callback do Firebase em uma Corrotina assincrona
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(true))
                    } else {
                        continuation.resume(Result.failure(task.exception ?: Exception("Erro desconhecido")))
                    }
                }
        }
    }

    override suspend fun signup(email: String, pass: String): Result<Boolean> {
        return suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(true))
                    } else {
                        continuation.resume(Result.failure(task.exception ?: Exception("Erro desconhecido")))
                    }
                }
        }
    }

    override fun logout() {
        auth.signOut()
    }
}