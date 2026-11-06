package com.hann.shopu3.StationeryInventory.Datos.Repositorio

import com.hann.shopu3.StationeryInventory.Datos.dto.ErrorDto
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class ApiException(val code: Int?, override val message: String) : Exception(message)

fun Throwable.toApiException(json: Json): ApiException = when (this) {
    is HttpException -> {
        val body = response()?.errorBody()?.string()
        val mensaje = body?.let {
            runCatching { json.decodeFromString<ErrorDto>(it).mensaje }.getOrNull()
        }
        ApiException(code(), mensaje ?: "Error del servidor (${code()})")
    }
    is IOException -> ApiException(null, "No se pudo conectar con el servidor. Revisa tu conexión")
    else -> ApiException(null, message ?: "Error inesperado")
}