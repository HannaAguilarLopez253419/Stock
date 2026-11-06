package com.hann.shopu3.StationeryInventory.Datos.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val precio: Double,
    val existencias: Int,
    val categoria: String? = null,
    val fechaIngreso: String
)