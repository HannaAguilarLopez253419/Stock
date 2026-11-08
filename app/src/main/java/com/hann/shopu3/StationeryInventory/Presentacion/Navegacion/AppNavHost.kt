package com.hann.shopu3.StationeryInventory.Presentacion.Navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.LoginPage
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.ProductDetailPage
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.ProductFormPage
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.ProductListPage
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.RegisterPage
import com.hann.shopu3.StationeryInventory.ShopApplication

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PRODUCTS = "products"
    const val PROFILE = "profile"
    const val PRODUCT_DETAIL = "product_detail/{id}"
    const val PRODUCT_FORM = "product_form?id={id}"

    fun detail(id: Int) = "product_detail/$id"
    fun form(id: Int? = null) = if (id == null) "product_form" else "product_form?id=$id"
}

private data class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomTabs = listOf(
    BottomTab(Routes.HOME, "Inicio", Icons.Filled.Home),
    BottomTab(Routes.PRODUCTS, "Productos", Icons.Filled.ShoppingCart),
    BottomTab(Routes.PROFILE, "Perfil", Icons.Filled.Person)
)

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

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = bottomTabs.any { it.route == currentRoute }

    Scaffold(
        containerColor = Color(0xFFFAF7FF),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(Routes.PRODUCTS) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF6B4BB8),
                                selectedTextColor = Color(0xFF6B4BB8),
                                indicatorColor = Color(0xFFE9DFFB),
                                unselectedIconColor = Color(0xFF5B5280),
                                unselectedTextColor = Color(0xFF5B5280)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {

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

            composable(Routes.HOME) {
                TemporaryScreen("Inicio")
            }

            composable(Routes.PRODUCTS) {
                ProductListPage(
                    onProductClick = { navController.navigate(Routes.detail(it)) },
                    onAddClick = { navController.navigate(Routes.form()) },
                    onLogout = logout,
                    onSessionExpired = logout
                )
            }

            composable(Routes.PROFILE) {
                TemporaryScreen("Perfil")
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
}

// Pantalla provisional: se reemplaza en los pasos siguientes
@Composable
private fun TemporaryScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title)
    }
}