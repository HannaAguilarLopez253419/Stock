package com.hann.shopu3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hann.shopu3.StationeryInventory.Presentacion.Navigation.AppNavHost
import com.hann.shopu3.StationeryInventory.Presentacion.Navigation.Routes
import com.hann.shopu3.StationeryInventory.ShopApplication
import com.hann.shopu3.ui.theme.ShopU3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as ShopApplication).container
        val startDestination =
            if (container.authRepository.isLoggedIn()) Routes.PRODUCTS else Routes.LOGIN

        setContent {
            ShopU3Theme {
                AppNavHost(startDestination = startDestination)
            }
        }
    }
}
