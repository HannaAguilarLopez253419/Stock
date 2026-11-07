package com.hann.shopu3.StationeryInventory.Datos.Repositorio

import com.hann.shopu3.StationeryInventory.Datos.Fuentededatos.ProductApiService
import com.hann.shopu3.StationeryInventory.Datos.mapper.toCreateDto
import com.hann.shopu3.StationeryInventory.Datos.mapper.toDomain
import com.hann.shopu3.StationeryInventory.Datos.mapper.toDomainList
import com.hann.shopu3.StationeryInventory.Datos.modelo.Product
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class ProductRepositoryImpl(
    private val apiService: ProductApiService,
    private val json: Json
) : ProductRepository {

    override suspend fun getProducts(search: String?): Result<List<Product>> = safeCall {
        apiService.getProducts(search?.trim()?.takeIf { it.isNotEmpty() }).toDomainList()
    }

    override suspend fun getProduct(id: Int): Result<Product> = safeCall {
        apiService.getProduct(id).toDomain()
    }

    override suspend fun createProduct(product: Product): Result<Product> = safeCall {
        apiService.createProduct(product.toCreateDto()).toDomain()
    }

    override suspend fun updateProduct(product: Product): Result<Product> = safeCall {
        apiService.updateProduct(product.id, product.toCreateDto()).toDomain()
    }

    override suspend fun deleteProduct(id: Int): Result<Unit> = safeCall {
        apiService.deleteProduct(id)
    }

    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toApiException(json))
        }
    }
}