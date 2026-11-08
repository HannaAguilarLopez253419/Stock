package com.hann.shopu3.StationeryInventory.Datos.Repositorio

import com.hann.shopu3.StationeryInventory.Datos.modelo.User

interface AuthRepository {
    suspend fun register(nombre: String, correo: String, password: String): Result<User>
    suspend fun login(correo: String, password: String): Result<User>
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): User?
    fun logout()
}