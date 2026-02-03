package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.navigation.TodoNavHost
import com.example.todolist.ui.feature.theme.ThemeViewModel
import com.example.todolist.ui.theme.ToDoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /** Claude (Sonnet 4.5) - início
     *
     * Implemente a função de troca de tema (dark/light)
     * baseado em `Theme.kt`
     *
     */
    private val themeViewModel: ThemeViewModel by lazy {
        ViewModelProvider(this)[ThemeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()


            ToDoListTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .safeDrawingPadding()
                    ) {
                        TodoNavHost(themeViewModel = themeViewModel)
                        /** Claude (Sonnet 4.5) - fim*/
                    }
                }
            }
        }
    }
}
