package com.hann.shopu3.StationeryInventory.Presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.ProductRepository
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.isUnauthorized
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.ProductListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadProducts() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = it.products.isEmpty(), errorMessage = null) }
        loadJob = viewModelScope.launch {
            productRepository.getProducts().fold(
                onSuccess = { list ->
                    _uiState.update { it.copy(products = list, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        when {
                            error.isUnauthorized() ->
                                it.copy(isLoading = false, sessionExpired = true)
                            it.products.isNotEmpty() ->
                                it.copy(isLoading = false, userMessage = error.message)
                            else ->
                                it.copy(
                                    isLoading = false,
                                    errorMessage = error.message ?: "No se pudieron cargar los productos"
                                )
                        }
                    }
                }
            )
        }
    }

    fun clearUserMessage() = _uiState.update { it.copy(userMessage = null) }
}