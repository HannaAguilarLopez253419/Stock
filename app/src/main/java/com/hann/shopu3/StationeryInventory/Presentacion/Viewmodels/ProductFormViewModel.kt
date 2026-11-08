package com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.ProductRepository
import com.hann.shopu3.StationeryInventory.Datos.Repositorio.isUnauthorized
import com.hann.shopu3.StationeryInventory.Datos.modelo.Product
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.States.ProductFormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProductFormViewModel(
    private val productRepository: ProductRepository,
    private val productId: Int?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductFormUiState(
            isEditing = productId != null,
            isLoading = productId != null,
            fechaIngreso = if (productId == null) todayAsText() else ""
        )
    )
    val uiState: StateFlow<ProductFormUiState> = _uiState.asStateFlow()

    init {
        if (productId != null) loadProduct(productId)
    }

    private fun loadProduct(id: Int) {
        viewModelScope.launch {
            productRepository.getProduct(id).fold(
                onSuccess = { p ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nombre = p.nombre,
                            codigo = p.codigo,
                            precio = p.precio.toString(),
                            existencias = p.existencias.toString(),
                            categoria = p.categoria.orEmpty(),
                            fechaIngreso = p.fechaIngreso
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        if (error.isUnauthorized()) {
                            it.copy(isLoading = false, sessionExpired = true)
                        } else {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "No se pudo cargar el producto"
                            )
                        }
                    }
                }
            )
        }
    }

    fun onNombreChange(value: String) =
        _uiState.update { it.copy(nombre = value, nombreError = null, errorMessage = null) }

    fun onCodigoChange(value: String) =
        _uiState.update { it.copy(codigo = value, codigoError = null, errorMessage = null) }

    fun onPrecioChange(value: String) =
        _uiState.update { it.copy(precio = value, precioError = null, errorMessage = null) }

    fun onExistenciasChange(value: String) =
        _uiState.update { it.copy(existencias = value, existenciasError = null, errorMessage = null) }

    fun onCategoriaChange(value: String) =
        _uiState.update { it.copy(categoria = value, errorMessage = null) }

    fun onFechaIngresoChange(value: String) =
        _uiState.update { it.copy(fechaIngreso = value, fechaIngresoError = null, errorMessage = null) }

    fun save() {
        val s = _uiState.value
        if (s.isSaving || s.isLoading) return

        val precio = s.precio.trim().replace(',', '.').toDoubleOrNull()
        val existencias = s.existencias.trim().toIntOrNull()

        val nombreError = if (s.nombre.isBlank()) "El nombre es obligatorio" else null
        val codigoError = if (s.codigo.isBlank()) "El código es obligatorio" else null
        val precioError = when {
            s.precio.isBlank() -> "El precio es obligatorio"
            precio == null -> "Escribe un número válido"
            precio < 0 -> "El precio no puede ser negativo"
            else -> null
        }
        val existenciasError = when {
            s.existencias.isBlank() -> "Las existencias son obligatorias"
            existencias == null -> "Escribe un número entero"
            existencias < 0 -> "Las existencias no pueden ser negativas"
            else -> null
        }
        val fechaError = when {
            s.fechaIngreso.isBlank() -> "La fecha es obligatoria"
            !isValidDate(s.fechaIngreso.trim()) -> "Usa el formato AAAA-MM-DD con una fecha real"
            else -> null
        }

        val hasErrors = listOf(nombreError, codigoError, precioError, existenciasError, fechaError)
            .any { it != null }
        if (hasErrors || precio == null || existencias == null) {
            _uiState.update {
                it.copy(
                    nombreError = nombreError,
                    codigoError = codigoError,
                    precioError = precioError,
                    existenciasError = existenciasError,
                    fechaIngresoError = fechaError
                )
            }
            return
        }

        val product = Product(
            id = productId ?: 0,
            nombre = s.nombre.trim(),
            codigo = s.codigo.trim(),
            precio = precio,
            existencias = existencias,
            categoria = s.categoria.trim().ifBlank { null },
            fechaIngreso = s.fechaIngreso.trim()
        )

        _uiState.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            val result = if (productId == null) {
                productRepository.createProduct(product)
            } else {
                productRepository.updateProduct(product)
            }
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isSaving = false, isSaved = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        if (error.isUnauthorized()) {
                            it.copy(isSaving = false, sessionExpired = true)
                        } else {
                            it.copy(
                                isSaving = false,
                                errorMessage = error.message ?: "No se pudo guardar el producto"
                            )
                        }
                    }
                }
            )
        }
    }
}

private fun todayAsText(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

private fun isValidDate(text: String): Boolean {
    if (!Regex("""\d{4}-\d{2}-\d{2}""").matches(text)) return false
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { isLenient = false }.parse(text) != null
    } catch (e: ParseException) {
        false
    }
}