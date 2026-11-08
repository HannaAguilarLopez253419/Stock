package com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.States

import com.hann.shopu3.StationeryInventory.Datos.modelo.Product

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val userMessage: String? = null,
    val sessionExpired: Boolean = false
){
    // Un producto tiene stock bajo si tiene 5 unidades o menos
    val lowStockProducts: List<Product>
        get() = products
            .filter { it.existencias <= LOW_STOCK_THRESHOLD }
            .sortedBy { it.existencias }
            .take(3)

    val totalUnits: Int
        get() = products.sumOf { it.existencias }

    val totalValue: Double
        get() = products.sumOf { it.precio * it.existencias }

    companion object {
        const val LOW_STOCK_THRESHOLD = 5
    }
}