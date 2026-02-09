    package com.example.todolist.ui.feature.list

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.todolist.data.TodoRepository
    import com.example.todolist.navigation.AddEditRoute
    import com.example.todolist.ui.UiEvent
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.channels.Channel
    import kotlinx.coroutines.flow.SharingStarted
    import kotlinx.coroutines.flow.receiveAsFlow
    import kotlinx.coroutines.flow.stateIn
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @HiltViewModel
    class ListViewModel @Inject constructor(
        // @Inject constructor antes dos parênteses
        private val repository: TodoRepository,
    ) : ViewModel() {

        val todos = repository.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        private val _uiEvent = Channel<UiEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()


        fun onEvent(event: ListEvent) {

            when (event) {
                is ListEvent.Delete -> {
                    delete(event.id)
                }

                is ListEvent.CompleteChanged -> {
                    completeChanged(event.id, event.isCompleted)
                }

                is ListEvent.AddEdit -> {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.Navigate(AddEditRoute(event.id)))
                    }

                }
            }
        }

        private fun delete(id: Long) {
            viewModelScope.launch {
                try {
                    repository.delete(id)
                    _uiEvent.send(UiEvent.ShowSnackbar("Tarefa excluída com sucesso!"))
                } catch (e: Exception) {
                    _uiEvent.send(UiEvent.ShowSnackbar("Erro ao excluir: ${e.message}"))
                }
            }
        }

        private fun completeChanged(id: Long, isCompleted: Boolean) {
            viewModelScope.launch {
                try {
                    repository.updateCompleted(id, isCompleted)
                    val message = if (isCompleted) {
                        "Tarefa marcada como concluída!"
                    } else {
                        "Tarefa marcada como pendente!"
                    }
                } catch (e: Exception) {
                    _uiEvent.send(UiEvent.ShowSnackbar("Erro ao atualizar tarefa: ${e.message}"))
                }
            }
        }
    }