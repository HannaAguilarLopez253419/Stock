package com.hann.shopu3.StationeryInventory.Datos.Repositorio

import com.hann.shopu3.StationeryInventory.Datos.Fuentededatos.AuthApiService
import com.hann.shopu3.StationeryInventory.Datos.Fuentededatos.SessionStorage
import com.hann.shopu3.StationeryInventory.Datos.dto.AuthResponseDto
import com.hann.shopu3.StationeryInventory.Datos.dto.LoginRequestDto
import com.hann.shopu3.StationeryInventory.Datos.dto.RegisterRequestDto
import com.hann.shopu3.StationeryInventory.Datos.mapper.toDomain
import com.hann.shopu3.StationeryInventory.Datos.modelo.User
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val sessionStorage: SessionStorage,
    private val json: Json
) : AuthRepository {

    override suspend fun register(nombre: String, correo: String, password: String): Result<User> =
        authenticate { apiService.register(
            RegisterRequestDto(
                nombre.trim(),
                correo.trim(),
                password
            )
        ) }

    override suspend fun login(correo: String, password: String): Result<User> =
        authenticate { apiService.login(LoginRequestDto(correo.trim(), password)) }

    override fun isLoggedIn(): Boolean = sessionStorage.getToken() != null

    override fun getCurrentUser(): User? {
        val id = sessionStorage.getUserId() ?: return null
        val nombre = sessionStorage.getUserName() ?: return null
        val correo = sessionStorage.getUserEmail() ?: return null
        return User(id = id, nombre = nombre, correo = correo)
    }

    override fun logout() = sessionStorage.clearSession()

    private suspend fun authenticate(call: suspend () -> AuthResponseDto): Result<User> {
        return try {
            val response = call()
            val user = response.usuario.toDomain()
            sessionStorage.saveToken(response.token)
            sessionStorage.saveUser(user.id, user.nombre, user.correo)
            Result.success(user)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toApiException(json))
        }
    }
}