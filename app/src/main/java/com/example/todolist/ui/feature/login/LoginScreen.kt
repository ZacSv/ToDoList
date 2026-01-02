package com.example.todolist.ui.feature.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.ui.viewmodel.AuthViewModel
import com.example.todolist.ui.viewmodel.AuthState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel, //Recebe VM
    navigateToHome: () -> Unit,
    navigateToSignup: () -> Unit
) {
    // A View observa o ViewModel (Padrão MVVM)
    val authState by viewModel.authState.observeAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Authenticated -> navigateToHome()
            is AuthState.Error -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it }, // Atualiza o estado local da View
            label = { Text("Senha") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            enabled = authState != AuthState.Loading
        ) {
            Text(text = "Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = navigateToSignup) {
            Text(text = "Não tem conta? Cadastre-se")
        }
    }
}