package com.example.todolist.data

import com.example.todolist.domain.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val dao: TodoDao
) : TodoRepository{

    override suspend fun insert(tittle: String, description: String?, id: Long?) {

        //Ou vai receber uma variável com uma entidade existente
        val entity = id?.let{
            dao.getById(it)?.copy(
                    title = tittle,
                    description = description
                )

            //Ou eh uma nova entidade
            } ?: TodoEntity(
                title = tittle,
                description = description,
                isCompleted = false
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
                    tittle = entity.title,
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
                tittle = entity.title,
                description = entity.description,
                isCompleted = entity.isCompleted
            )
        }
    }
}