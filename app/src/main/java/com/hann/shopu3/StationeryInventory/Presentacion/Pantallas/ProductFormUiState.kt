package com.hann.shopu3.StationeryInventory.Presentacion.Pantallas

data class ProductFormUiState(
    val isEditing: Boolean = false,
    val nombre: String = "",
    val codigo: String = "",
    val precio: String = "",
    val existencias: String = "",
    val categoria: String = "",
    val fechaIngreso: String = "",
    val nombreError: String? = null,
    val codigoError: String? = null,
    val precioError: String? = null,
    val existenciasError: String? = null,
    val fechaIngresoError: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false,
    val sessionExpired: Boolean = false
)