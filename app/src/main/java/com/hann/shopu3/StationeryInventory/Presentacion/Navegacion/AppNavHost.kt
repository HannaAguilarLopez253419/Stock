package com.hann.shopu3.StationeryInventory.Presentacion.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hann.shopu3.StationeryInventory.Presentacion.pantallas.LoginPage
import com.hann.shopu3.StationeryInventory.Presentacion.pantallas.RegisterPage
import com.hann.shopu3.StationeryInventory.ShopApplication

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRODUCTS = "products"
}

@Composable
fun AppNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginPage(
                onLoginSuccess = {
                    navController.navigate(Routes.PRODUCTS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterPage(
                onRegisterSuccess = {
                    navController.navigate(Routes.PRODUCTS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.PRODUCTS) {
            // TEMPORAL: se reemplaza por la lista real del CRUD
            val container = (LocalContext.current.applicationContext as ShopApplication).container
            Scaffold { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Home (temporal)", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = {
                        container.authRepository.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.PRODUCTS) { inclusive = true }
                        }
                    }) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    }
}