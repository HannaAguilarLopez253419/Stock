package com.hann.shopu3.StationeryInventory.Presentacion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hann.shopu3.StationeryInventory.Presentacion.pantallas.LoginPage
import com.hann.shopu3.StationeryInventory.Presentacion.pantallas.ProductDetailPage
import com.hann.shopu3.StationeryInventory.Presentacion.pantallas.ProductFormPage
import com.hann.shopu3.StationeryInventory.Presentacion.pantallas.ProductListPage
import com.hann.shopu3.StationeryInventory.Presentacion.pantallas.RegisterPage
import com.hann.shopu3.StationeryInventory.ShopApplication

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRODUCTS = "products"
    const val PRODUCT_DETAIL = "product_detail/{id}"
    const val PRODUCT_FORM = "product_form?id={id}"

    fun detail(id: Int) = "product_detail/$id"
    fun form(id: Int? = null) = if (id == null) "product_form" else "product_form?id=$id"
}

@Composable
fun AppNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    val container = (LocalContext.current.applicationContext as ShopApplication).container

       val logout: () -> Unit = {
        container.authRepository.logout()
        navController.navigate(Routes.LOGIN) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    val goToProducts: () -> Unit = {
        navController.navigate(Routes.PRODUCTS) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginPage(
                onLoginSuccess = goToProducts,
                onGoToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterPage(
                onRegisterSuccess = goToProducts,
                onGoToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.PRODUCTS) {
            ProductListPage(
                onProductClick = { navController.navigate(Routes.detail(it)) },
                onAddClick = { navController.navigate(Routes.form()) },
                onLogout = logout,
                onSessionExpired = logout
            )
        }

        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: return@composable
            ProductDetailPage(
                productId = id,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.form(it)) },
                onDeleted = { navController.popBackStack() },
                onSessionExpired = logout
            )
        }

        composable(
            route = Routes.PRODUCT_FORM,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            ProductFormPage(
                productId = id.takeIf { it != -1 },
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
                onSessionExpired = logout
            )
        }
    }
}