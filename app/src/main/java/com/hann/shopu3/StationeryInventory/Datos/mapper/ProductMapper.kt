package com.hann.shopu3.StationeryInventory.Datos.mapper

import com.hann.shopu3.StationeryInventory.Datos.dto.CreateProductDto
import com.hann.shopu3.StationeryInventory.Datos.dto.ProductDto
import com.hann.shopu3.StationeryInventory.Datos.modelo.Product

fun ProductDto.toDomain(): Product = Product(
    id = id,
    nombre = nombre,
    codigo = codigo,
    precio = precio,
    existencias = existencias,
    categoria = categoria,
    fechaIngreso = fechaIngreso
)

fun List<ProductDto>.toDomainList(): List<Product> = map { it.toDomain() }

fun Product.toCreateDto(): CreateProductDto = CreateProductDto(
    nombre = nombre,
    codigo = codigo,
    precio = precio,
    existencias = existencias,
    categoria = categoria,
    fechaIngreso = fechaIngreso
)