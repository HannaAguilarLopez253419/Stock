package com.hann.shopu3.StationeryInventory.Datos.Fuentededatos


import android.content.Context

class SharedPreferencesSessionStorage(context: Context) : SessionStorage {

    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    override fun saveUser(id: Int, nombre: String, correo: String) {
        prefs.edit()
            .putInt(KEY_USER_ID, id)
            .putString(KEY_USER_NAME, nombre)
            .putString(KEY_USER_EMAIL, correo)
            .apply()
    }

    override fun getUserId(): Int? =
        if (prefs.contains(KEY_USER_ID)) prefs.getInt(KEY_USER_ID, -1) else null

    override fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)

    override fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)

    override fun clearSession() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_TOKEN = "token"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
    }
}