package com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val nombre: String = "",
    val correo: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nombreError: String? = null,
    val correoError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNombreChange(value: String) =
        _uiState.update { it.copy(nombre = value, nombreError = null, errorMessage = null) }

    fun onCorreoChange(value: String) =
        _uiState.update { it.copy(correo = value, correoError = null, errorMessage = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, passwordError = null, errorMessage = null) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, errorMessage = null) }

    fun register() {
        val s = _uiState.value
        if (s.isLoading) return

        val nombreError = if (s.nombre.isBlank()) "El nombre es obligatorio" else null
        val correoError = when {
            s.correo.isBlank() -> "El correo es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(s.correo.trim()).matches() -> "El correo no es válido"
            else -> null
        }
        val passwordError = when {
            s.password.isEmpty() -> "La contraseña es obligatoria"
            s.password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            else -> null
        }
        val confirmError = when {
            s.confirmPassword.isEmpty() -> "Confirma tu contraseña"
            s.confirmPassword != s.password -> "Las contraseñas no coinciden"
            else -> null
        }

        if (listOf(nombreError, correoError, passwordError, confirmError).any { it != null }) {
            _uiState.update {
                it.copy(
                    nombreError = nombreError,
                    correoError = correoError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            authRepository.register(s.nombre, s.correo, s.password).fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Error al registrarse")
                    }
                }
            )
        }
    }

    fun onNavigated() = _uiState.update { it.copy(isSuccess = false) }
}