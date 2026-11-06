package com.hann.shopu3.StationeryInventory.Datos.Fuentededatos

import com.hann.shopu3.StationeryInventory.Datos.dto.CreateProductDto
import com.hann.shopu3.StationeryInventory.Datos.dto.ProductDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("productos")
    suspend fun getProducts(@Query("buscar") search: String? = null): List<ProductDto>

    @GET("productos/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductDto

    @POST("productos")
    suspend fun createProduct(@Body body: CreateProductDto): ProductDto

    @PUT("productos/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body body: CreateProductDto): ProductDto

    @DELETE("productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Int)
}