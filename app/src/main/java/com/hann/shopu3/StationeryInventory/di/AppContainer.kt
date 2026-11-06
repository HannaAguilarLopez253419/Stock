package com.hann.shopu3.StationeryInventory.di

import android.content.Context
import com.hann.shopu3.StationeryInventory.Datos.Fuentededatos.AuthApiService
import com.hann.shopu3.StationeryInventory.Datos.Fuentededatos.AuthInterceptor
import com.hann.shopu3.StationeryInventory.Datos.Fuentededatos.ProductApiService
import com.hann.shopu3.StationeryInventory.Datos.Fuentededatos.SessionStorage
import com.hann.shopu3.StationeryInventory.Datos.Fuentededatos.SharedPreferencesSessionStorage
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppContainer(context: Context) {

    // 10.0.2.2 es la forma en que el emulador llega a tu computadora
    private val baseUrl = "http://10.0.2.2:3000/"

    private val json = Json { ignoreUnknownKeys = true }

    val sessionStorage: SessionStorage by lazy {
        SharedPreferencesSessionStorage(context)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionStorage))
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val authApiService: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
    val productApiService: ProductApiService by lazy { retrofit.create(ProductApiService::class.java) }
}