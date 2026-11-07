package com.hann.shopu3.StationeryInventory.Presentacion.Pantallas

import com.hann.shopu3.StationeryInventory.Datos.modelo.Product

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userMessage: String? = null,
    val sessionExpired: Boolean = false
)