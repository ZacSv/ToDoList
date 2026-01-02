package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.todolist.ui.feature.addedit.AddEditScreen
import com.example.todolist.ui.feature.auth.LoginScreen
import com.example.todolist.ui.feature.auth.SignupScreen
import com.example.todolist.ui.feature.list.ListScreen
import com.example.todolist.ui.viewmodel.AuthViewModel
import kotlinx.serialization.Serializable

// --- DEFINIÇÃO DAS ROTAS ---
@Serializable
object LoginRoute

@Serializable
object SignupRoute

@Serializable
object ListRoute

@Serializable
data class AddEditRoute(val id: Long? = null)

@Composable
fun TodoNavHost() {
    val navController = rememberNavController()

    // Injetamos o AuthViewModel aqui para compartilhar a mesma instância entre Login e Signup
    val authViewModel: AuthViewModel = hiltViewModel()

    // Agora o startDestination aponta para um objeto que existe (definido acima)
    NavHost(navController = navController, startDestination = LoginRoute) {

        // --- TELA DE LOGIN ---
        composable<LoginRoute> {
            LoginScreen(
                viewModel = authViewModel,
                navigateToHome = {
                    // Ao logar, vai para a Lista e remove o Login da pilha (para não voltar com botão "Voltar")
                    navController.navigate(ListRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                navigateToSignup = {
                    navController.navigate(SignupRoute)
                }
            )
        }

        // --- TELA DE CADASTRO ---
        composable<SignupRoute> {
            SignupScreen(
                viewModel = authViewModel,
                navigateToHome = {
                    navController.navigate(ListRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                navigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable<ListRoute> {
            ListScreen(
                navigateToAddEditScreen = { id ->
                    navController.navigate(AddEditRoute(id = id))
                }
            )
        }

        composable<AddEditRoute> { backStackEntry ->
            val addEditRoute = backStackEntry.toRoute<AddEditRoute>()
            AddEditScreen(
                id = addEditRoute.id,
                navigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}