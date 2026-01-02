package com.example.todolist.ui.feature.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoRepository
import com.example.todolist.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val id: Long? = null,
    private val repository: TodoRepository,
) : ViewModel() {

    var tittle by mutableStateOf("")
        private set
    var description by mutableStateOf<String?>("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init{
        id?.let{
            viewModelScope.launch {
                    val todo = repository.getById(it)
                    tittle = todo?.tittle ?: ""
                    description = todo?.description

                }
            }
        }


    fun onEvent(event: AddEditEvent) {

        when (event) {
            is AddEditEvent.TitleChanged -> {
                tittle = event.title
            }

            is AddEditEvent.DescriptionChanged -> {
                description = event.description
            }

            is AddEditEvent.Save -> {
                save()
            }
        }
    }

    private fun save()
    {
        viewModelScope.launch {

            //Se o título for nulo, retorna sem inserir;
            if(tittle.isBlank())
            {
                _uiEvent.send(UiEvent.ShowSnackbar( "O título não pode estar vazio"
                ))
                return@launch
            }
            repository.insert(tittle, description, id)
            _uiEvent.send(UiEvent.NavigateBack)

        }
    }

}