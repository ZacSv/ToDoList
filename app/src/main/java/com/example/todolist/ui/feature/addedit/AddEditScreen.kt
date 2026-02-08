package com.example.todolist.ui.feature.addedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // <--- Importante: Import do Hilt
import com.example.todolist.ui.UiEvent
import com.example.todolist.ui.feature.theme.ThemeViewModel
import com.example.todolist.ui.theme.ToDoListTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditScreen(
    id: Long?,
    navigateBack: () -> Unit,
    onLogout: () -> Unit,
    themeViewModel: ThemeViewModel,
    viewModel: AddEditViewModel = hiltViewModel(), // <--- A Mágica do Hilt acontece aqui
) {

    val title = viewModel.title
    val description = viewModel.description
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    val screenTitle = if (id == null || id == -1L) "Nova Tarefa" else "Editar Tarefa"

    val snackbarHostState = remember { SnackbarHostState() }

    /* GEMINI PRO - START
     Prompt:
     I'm creating an AddEditScreen in my ToDoList app. I already have the text fields saving the state in the ViewModel, but I'm having trouble finalizing the screen.
     The scenario is: When the user clicks the 'Check' (save) button, the ViewModel does its job in the database. I want that, as soon as the saving is finished, the app displays a notification (Snackbar) and automatically returns to the previous screen.
     I saw that it's not good to use a 'state' variable (like shouldExit = true) because if the user rotates the screen, the state might trigger navigation again. I was told to use an event Flow in the ViewModel, but how do I 'listen' to this Flow in my Composable in a way that it doesn't miss events if they arrive one after the other?
     What is collectLatest and why should I use it inside a LaunchedEffect(true) to navigate back?*/

    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message,
                        duration = SnackbarDuration.Short
                    )
                }

                UiEvent.NavigateBack -> {
                    navigateBack()
                }

                is UiEvent.Navigate<*> -> {
                    // Navegação futura
                }
            }
        }
    }
    /* GEMINI PRO - END  */
    AddEditContent(
        title = title,
        description = description,
        screenTitle = screenTitle,
        onEvent = viewModel::onEvent,
        onBackClick = navigateBack,
        onLogout = onLogout,
        isDarkTheme = isDarkTheme,
        onToggleTheme = { themeViewModel.toggleTheme() },
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContent(
    title: String,
    description: String?,
    screenTitle: String,
    snackbarHostState: SnackbarHostState,
    onEvent: (AddEditEvent) -> Unit,
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false); }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text = screenTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (isDarkTheme) "Modo Claro" else "Modo Escuro"
                                )
                            },
                            onClick = {
                                onToggleTheme()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (isDarkTheme)
                                        Icons.Default.LightMode
                                    else
                                        Icons.Default.DarkMode,
                                    contentDescription = null
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Sair") },
                            onClick = {
                                showMenu = false
                                onLogout()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(AddEditEvent.Save)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Check, "Save")
            }
        }, snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface
                ),
                value = title,
                onValueChange = {
                onEvent(
                    AddEditEvent.TitleChanged(it)
                )
            }, placeholder = {
                Text(text = "Título")
            })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface
                ),
                value = description ?: "",
                onValueChange = {
                    onEvent(
                        AddEditEvent.DescriptionChanged(it)
                    )
                },
                placeholder = {
                    Text(text = "Descrição (opcional)")
                })
        }
    }
}

@Preview
@Composable
private fun AddEditContentPreview() {
    ToDoListTheme {
        AddEditContent(
            title = "",
            description = null,
            screenTitle = "Nova Tarefa",
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            onBackClick = {},
            onLogout = {},
            isDarkTheme = false,
            onToggleTheme = {}
        )
    }
}

@Preview
@Composable
private fun AddEditContentPreview2() {
    ToDoListTheme {
        AddEditContent(
            title = "",
            description = null,
            screenTitle = "Editar Tarefa",
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            onBackClick = {},
            onLogout = {},
            isDarkTheme = false,
            onToggleTheme = {}
        )
    }
}