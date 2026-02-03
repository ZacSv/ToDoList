package com.example.todolist.ui.feature.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.todolist.data.TodoRepository
import com.example.todolist.navigation.AddEditRoute
import com.example.todolist.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel // Importante
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject // Importante

@HiltViewModel
class AddEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, //Recebe os argumentos da navegação aqui
    private val repository: TodoRepository,
) : ViewModel() {

    //Recupera o ID automaticamente usando a rota definida
    private val todoId: Long? = savedStateHandle.toRoute<AddEditRoute>().id

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf<String?>("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        //Se tiver ID, carrega os dados do Firestore
        todoId?.let { id ->
            viewModelScope.launch {
                val todo = repository.getById(id)
                title = todo?.title ?: ""
                description = todo?.description
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.TitleChanged -> {
                title = event.title
            }

            is AddEditEvent.DescriptionChanged -> {
                description = event.description
            }

            is AddEditEvent.Save -> {
                save()
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            if (title.isBlank()) {
                _uiEvent.send(UiEvent.ShowSnackbar("O título não pode estar vazio"))
                return@launch
            }

            try {
                // Salva no Firestore
                repository.insert(title, description, todoId)

                /*val message = if (todoId == null) {
                    "Tarefa criada com sucesso"
                } else {
                    "Tarefa atualizada com sucesso"
                }
                _uiEvent.send(UiEvent.ShowSnackbar(message))

                kotlinx.coroutines.delay(300)*/

                // Volta para a tela anterior
                _uiEvent.send(UiEvent.NavigateBack)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.ShowSnackbar("Erro ao salvar tarefa: ${e.message}"))
            }
        }
    }
}