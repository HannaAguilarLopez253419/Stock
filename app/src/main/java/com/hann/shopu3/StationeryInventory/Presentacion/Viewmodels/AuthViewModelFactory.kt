package com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.AuthRepository

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
            RegisterViewModel(authRepository) as T
        modelClass.isAssignableFrom(LoginViewModel::class.java) ->
            LoginViewModel(authRepository) as T
        else -> throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}