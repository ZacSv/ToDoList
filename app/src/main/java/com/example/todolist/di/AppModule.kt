package com.example.todolist.di

import com.example.todolist.data.AuthRepository
import com.example.todolist.data.AuthRepositoryImpl
import com.example.todolist.data.FirestoreRepositoryImpl
import com.example.todolist.data.TodoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- FIREBASE ---
    // Ensina o Hilt a pegar a instância do Auth
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // Ensina o Hilt a pegar a instância do Firestore (Banco de Dados)
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    // --- REPOSITÓRIOS ---

    // Ensina o Hilt a criar o AuthRepository
    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): TodoRepository {
        return FirestoreRepositoryImpl(firestore, auth)
    }
}