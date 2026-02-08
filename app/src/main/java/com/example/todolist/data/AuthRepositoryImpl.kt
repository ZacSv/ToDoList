package com.example.todolist.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume


/* GEMINI PRO - START 1
 Prompt:
 I'm developing a ToDoList app in Kotlin with Jetpack Compose and I'm using Hilt for dependency injection. I need to create my AuthRepositoryImpl that inherits from an AuthRepository interface.
The repository needs to use FirebaseAuth to:
Get the current user.
Log in.
Register.
Log out.
The detail: I want to use Coroutines (suspend functions) in my ViewModel, but Firebase works with those addOnCompleteListener callbacks. How can I transform these Firebase methods into suspended functions that return a Result<Boolean> so I can handle the error or success later?
You can use @Inject in the constructor to pass FirebaseAuth for Hilt.*/

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser



    /* GEMINI PRO - START 2
     Prompt:
     I'm trying to create the login function in my repository using Firebase, but it uses a callback.
     The problem is that this leaves the code 'stuck' inside, and I wanted my function to suspend so I could call it directly in my ViewModel in an asynchronous and clean way. I read that it's possible to 'convert' these callbacks into coroutines, but I don't know how to do that safely.
     Transform this Firebase signInWithEmailAndPassword method into a function that I can await or something similar, and that returns a Result<Boolean>? Oh, and if the login fails, I need the Firebase exception to come inside this Result so I know what happened.*/

    override suspend fun login(email: String, pass: String): Result<Boolean> {
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

    /* GEMINI PRO - END  2 */

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

    /* GEMINI PRO - END 1 */
}