package com.hann.shopu3.StationeryInventory.Datos.Fuentededatos

import android.content.Context

class SharedPreferencesSessionStorage(context: Context) : SessionStorage {

    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    override fun clearSession() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    private companion object {
        const val KEY_TOKEN = "token"
    }
}