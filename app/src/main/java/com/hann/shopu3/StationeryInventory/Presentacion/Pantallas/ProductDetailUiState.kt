package com.hann.shopu3.StationeryInventory.Presentacion.Pantallas

import com.hann.shopu3.StationeryInventory.Datos.modelo.Product

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val userMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val sessionExpired: Boolean = false
)