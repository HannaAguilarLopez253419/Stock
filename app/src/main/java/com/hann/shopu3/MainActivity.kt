package com.hann.shopu3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hann.shopu3.StationeryInventory.ShopApplication
import com.hann.shopu3.ui.theme.ShopU3Theme
import android.graphics.Color
import androidx.activity.SystemBarStyle
import com.hann.shopu3.StationeryInventory.Presentacion.Navegacion.AppNavHost
import com.hann.shopu3.StationeryInventory.Presentacion.Navegacion.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        val container = (application as ShopApplication).container
        val startDestination =
            if (container.authRepository.isLoggedIn()) Routes.HOME else Routes.LOGIN
        setContent {
            ShopU3Theme {
                AppNavHost(startDestination = startDestination)
            }
        }
    }
}
