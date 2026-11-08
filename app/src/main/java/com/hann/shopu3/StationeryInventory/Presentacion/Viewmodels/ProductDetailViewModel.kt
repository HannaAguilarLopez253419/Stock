package com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.ProductRepository
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.isUnauthorized
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.States.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val productId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct() {
        _uiState.update { it.copy(isLoading = it.product == null, errorMessage = null) }
        viewModelScope.launch {
            productRepository.getProduct(productId).fold(
                onSuccess = { product ->
                    _uiState.update { it.copy(product = product, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        when {
                            error.isUnauthorized() ->
                                it.copy(isLoading = false, sessionExpired = true)
                            it.product != null ->
                                it.copy(isLoading = false, userMessage = error.message)
                            else ->
                                it.copy(
                                    isLoading = false,
                                    errorMessage =       error.message ?: "No se pudo cargar el producto"
                                )
                        }
                    }
                }
            )
        }
    }

    fun onDeleteClick() = _uiState.update { it.copy(showDeleteDialog = true) }

    fun onDismissDelete() = _uiState.update { it.copy(showDeleteDialog = false) }

    fun confirmDelete() {
        if (_uiState.value.isDeleting) return
        _uiState.update { it.copy(showDeleteDialog = false, isDeleting = true) }
        viewModelScope.launch {
            productRepository.deleteProduct(productId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeleting = false, isDeleted = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        if (error.isUnauthorized()) {
                            it.copy(isDeleting = false, sessionExpired = true)
                        } else {
                            it.copy(
                                isDeleting = false,
                                userMessage = error.message ?: "No se pudo eliminar el producto"
                            )
                        }
                    }
                }
            )
        }
    }

    fun clearUserMessage() = _uiState.update { it.copy(userMessage = null) }
}