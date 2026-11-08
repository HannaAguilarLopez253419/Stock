package com.hann.shopu3.StationeryInventory.Datos.Fuentededatos

interface SessionStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun saveUser(id: Int, nombre: String, correo: String)
    fun getUserId(): Int?
    fun getUserName(): String?
    fun getUserEmail(): String?
    fun clearSession()
}