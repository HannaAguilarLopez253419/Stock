package com.hann.shopu3.StationeryInventory.Datos.modelo

data class Product(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val precio: Double,
    val existencias: Int,
    val categoria: String?,
    val fechaIngreso: String
)