package com.hann.shopu3.StationeryInventory.Datos.Repositorio

import com.hann.shopu3.StationeryInventory.Datos.modelo.Product

interface ProductRepository {
    suspend fun getProducts(search: String? = null): Result<List<Product>>
    suspend fun getProduct(id: Int): Result<Product>
    suspend fun createProduct(product: Product): Result<Product>
    suspend fun updateProduct(product: Product): Result<Product>
    suspend fun deleteProduct(id: Int): Result<Unit>
}