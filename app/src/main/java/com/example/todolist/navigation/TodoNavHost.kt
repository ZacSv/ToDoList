package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.todolist.ui.feature.addedit.AddEditScreen
import com.example.todolist.ui.feature.auth.AuthViewModel
import com.example.todolist.ui.feature.auth.LoginScreen
import com.example.todolist.ui.feature.auth.SignupScreen
import com.example.todolist.ui.feature.list.ListScreen
import kotlinx.serialization.Serializable

// --- ROTAS ---
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
    val authViewModel: AuthViewModel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current

    NavHost(navController = navController, startDestination = LoginRoute) {

        // --- LOGIN ---
        composable<LoginRoute> {
            LoginScreen(
                viewModel = authViewModel,
                navigateToHome = {
                    navController.navigate(ListRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                navigateToSignup = {
                    navController.navigate(SignupRoute)
                }
            )
        }

        // --- CADASTRO ---
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

        // --- LISTA ---
        composable<ListRoute> {
            ListScreen(
                navigateToAddEditScreen = { id ->
                    navController.navigate(AddEditRoute(id = id))
                },
                onLogout = {
                    keyboardController?.hide()

                    authViewModel.signout()

                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // --- ADICIONAR/EDITAR ---
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