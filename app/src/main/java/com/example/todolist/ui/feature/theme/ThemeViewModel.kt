package com.example.todolist.ui.feature.theme

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Claude (Sonnet 4.5) - início
 *
 * Implemente a função de troca de tema (dark/light)
 * baseado em `Theme.kt`
 *
 */
class ThemeViewModel : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
}
/** Claude (Sonnet 4.5) - fim*/