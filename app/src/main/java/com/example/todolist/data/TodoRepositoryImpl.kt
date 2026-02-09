package com.example.todolist.data

import com.example.todolist.domain.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val dao: TodoDao
) : TodoRepository {

    // Ajuste aqui: adicionei "isCompleted: Boolean" para bater com a Interface
    override suspend fun insert(tittle: String, description: String?, id: Long?, isCompleted: Boolean) {

        val entity = id?.let { idExistente ->
            // Tenta buscar o existente
            dao.getById(idExistente)?.copy(
                title = tittle,
                description = description,
                isCompleted = isCompleted // <--- CORREÇÃO: Atualiza o status com o valor que veio da tela
            )
            // Se por acaso o ID não existir no banco (borda), cria um novo com esse ID
                ?: TodoEntity(
                    id = idExistente,
                    title = tittle,
                    description = description,
                    isCompleted = isCompleted
                )
        } ?: TodoEntity(
            // Nova entidade (id null)
            title = tittle,
            description = description,
            isCompleted = isCompleted // <--- CORREÇÃO: Usa o valor recebido (que pode ser true ou false)
        )

        dao.insert(entity)
    }

    override suspend fun updateCompleted(id: Long, isCompleted: Boolean) {
        val existingEntity = dao.getById(id) ?: return
        val updatedEntity = existingEntity.copy(isCompleted = isCompleted)
        dao.insert(updatedEntity)
    }

    override suspend fun delete(id: Long) {
        val existingEntity = dao.getById(id) ?: return
        dao.delete(existingEntity)
    }

    override fun getAll(): Flow<List<Todo>> {
        return dao.getAll().map { entities ->
            entities.map { entity ->
                Todo(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    isCompleted = entity.isCompleted
                )
            }
        }
    }

    override suspend fun getById(id: Long): Todo? {
        return dao.getById(id)?.let { entity ->
            Todo(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                isCompleted = entity.isCompleted
            )
        }
    }
}