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
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit
) {
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
        Text(text = "Cadastro", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.signup(email, password) },
            enabled = authState != AuthState.Loading
        ) {
            Text(text = "Criar Conta")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = navigateToLogin) {
            Text(text = "Já tem conta? Faça Login")
        }
    }
}