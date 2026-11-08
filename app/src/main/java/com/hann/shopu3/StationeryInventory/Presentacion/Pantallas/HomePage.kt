package com.hann.shopu3.StationeryInventory.Presentacion.Pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hann.shopu3.StationeryInventory.Datos.modelo.Product
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.AppTopBar
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.ErrorState
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.StatusBarIcons
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.ProductListViewModel
import com.hann.shopu3.StationeryInventory.ShopApplication

@Composable
fun HomePage(
    onGoToProducts: () -> Unit,
    onProductClick: (Int) -> Unit,
    onLogout: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: ProductListViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as ShopApplication)
            .container.productViewModelFactory()
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StatusBarIcons(darkIcons = false)

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.loadProducts() }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) onSessionExpired()
    }

    HomeContent(
        uiState = uiState,
        onGoToProducts = onGoToProducts,
        onProductClick = onProductClick,
        onRetry = viewModel::loadProducts,
        onLogout = onLogout
    )
}

@Composable
fun HomeContent(
    uiState: ProductListUiState,
    onGoToProducts: () -> Unit,
    onProductClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onLogout: () -> Unit
) {
    // Un producto se considera con stock bajo si tiene 5 unidades o menos
    val lowStock = uiState.products
        .filter { it.existencias <= 5 }
        .sortedBy { it.existencias }
        .take(3)
    val totalUnits = uiState.products.sumOf { it.existencias }
    val totalValue = uiState.products.sumOf { it.precio * it.existencias }

    Scaffold(
        containerColor = Color(0xFFFAF7FF),
        topBar = {
            AppTopBar(
                title = "Inventario Papelería",
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                uiState.errorMessage != null -> ErrorState(
                    message = uiState.errorMessage.orEmpty(),
                    onRetry = onRetry
                )

                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9DFFB))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "¡Bienvenido/a!",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF4B2E8F)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Gestiona tu inventario fácilmente.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF5B5280)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Filled.ShoppingCart,
                            label = "Productos",
                            value = uiState.products.size.toString()
                        )
                        SummaryCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Filled.Check,
                            label = "Unidades en stock",
                            value = totalUnits.toString()
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE9DFFB))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Valor del inventario",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF5B5280)
                            )
                            Text(
                                text = "\$%.2f".format(totalValue),
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color(0xFF1E1747),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "Stock bajo",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF1E1747)
                    )

                    if (lowStock.isEmpty()) {
                        Text(
                            text = "Todo en orden: no hay productos con stock bajo.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF5B5280)
                        )
                    } else {
                        lowStock.forEach { product ->
                            LowStockItem(product = product, onClick = { onProductClick(product.id) })
                        }
                    }

                    OutlinedButton(
                        onClick = onGoToProducts,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF6B4BB8)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6B4BB8)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Ver productos")
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE9DFFB))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE9DFFB), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF6B4BB8))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF1E1747),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5B5280)
            )
        }
    }
}

@Composable
private fun LowStockItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE9DFFB))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFDECEC), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1E1747)
                )
                Text(
                    text = "Stock: ${product.existencias}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD32F2F)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF6B4BB8)
            )
        }
    }
}