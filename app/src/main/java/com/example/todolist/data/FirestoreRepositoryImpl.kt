package com.example.todolist.data

import com.example.todolist.domain.Todo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : TodoRepository {

    private fun getTodosCollection(): CollectionReference? {
        val uid = auth.currentUser?.uid ?: return null
        return firestore.collection("users").document(uid).collection("todos")
    }

    override suspend fun insert(tittle: String, description: String?, id: Long?) {
        val collection = getTodosCollection() ?: return
        val todoId = id ?: System.currentTimeMillis()
        val todoMap = hashMapOf(
            "id" to todoId,
            "title" to tittle,
            "description" to description,
            "isCompleted" to false
        )
        try {
            collection.document(todoId.toString()).set(todoMap).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateCompleted(id: Long, isCompleted: Boolean) {
        val collection = getTodosCollection() ?: return
        try {
            collection.document(id.toString())
                .update("isCompleted", isCompleted).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(id: Long) {
        val collection = getTodosCollection() ?: return
        try {
            collection.document(id.toString()).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getAll(): Flow<List<Todo>> {
        val collection = getTodosCollection()

        // Se já estiver deslogado ao chamar, nem tenta conectar
        if (collection == null) {
            return flow { emit(emptyList()) }
        }

        return callbackFlow {
            val subscription = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // --- A CORREÇÃO DO CRASH ESTÁ AQUI ---
                    // Se o erro for PERMISSION_DENIED (acontece no logout),
                    // fechamos o fluxo silenciosamente sem explodir o app.
                    if (error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        close()
                        return@addSnapshotListener
                    }

                    // Outros erros reais nós repassamos
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val todos = snapshot.documents.mapNotNull { doc ->
                        val id = doc.getLong("id")
                        val title = doc.getString("title")
                        val desc = doc.getString("description")
                        val completed = doc.getBoolean("isCompleted") ?: false

                        if (id != null && title != null) {
                            Todo(
                                id = id,
                                tittle = title,
                                description = desc,
                                isCompleted = completed
                            )
                        } else null
                    }
                    trySend(todos)
                }
            }
            awaitClose { subscription.remove() }
        }
    }

    override suspend fun getById(id: Long): Todo? {
        val collection = getTodosCollection() ?: return null
        return try {
            val doc = collection.document(id.toString()).get().await()
            if (doc.exists()) {
                Todo(
                    id = doc.getLong("id")!!,
                    tittle = doc.getString("title")!!,
                    description = doc.getString("description"),
                    isCompleted = doc.getBoolean("isCompleted") ?: false
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}