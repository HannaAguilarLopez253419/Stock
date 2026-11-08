package com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.AuthRepository
import com.hann.shopu3.StationeryInventory.Datos.modelo.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val user: User? = null
)

class ProfileViewModel(
    authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(user = authRepository.getCurrentUser())
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
}

class ProfileViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(authRepository) as T
    }
}