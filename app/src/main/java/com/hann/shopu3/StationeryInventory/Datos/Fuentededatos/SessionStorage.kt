package com.hann.shopu3.StationeryInventory.Datos.Fuentededatos

interface SessionStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearSession()
}