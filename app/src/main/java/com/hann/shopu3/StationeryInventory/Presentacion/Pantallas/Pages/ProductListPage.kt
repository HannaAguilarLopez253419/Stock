package com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.Pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hann.shopu3.StationeryInventory.Datos.modelo.Product
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.AppTopBar
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.ErrorState
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.StatusBarIcons
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.States.ProductListUiState
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.ProductListViewModel
import com.hann.shopu3.StationeryInventory.ShopApplication

@Composable
fun ProductListPage(
    onProductClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onLogout: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: ProductListViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as ShopApplication)
            .container.productViewModelFactory()
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    StatusBarIcons(darkIcons = false)

    // Recarga cada vez que la pantalla vuelve a estar visible
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.loadProducts() }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) onSessionExpired()
    }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    ProductListContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onProductClick = onProductClick,
        onAddClick = onAddClick,
        onRetry = viewModel::loadProducts,
        onLogout = onLogout
    )
}

@Composable
fun ProductListContent(
    uiState: ProductListUiState,
    snackbarHostState: SnackbarHostState,
    onProductClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onRetry: () -> Unit,
    onLogout: () -> Unit
) {
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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

                uiState.products.isEmpty() -> EmptyState(onAddClick = onAddClick)

                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { WelcomeCard() }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Productos (${uiState.products.size})",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF1E1747)
                            )
                            AddButton(onClick = onAddClick)
                        }
                    }
                    items(uiState.products, key = { it.id }) { product ->
                        ProductItem(product = product, onClick = { onProductClick(product.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9DFFB))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
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
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint = Color(0xFF6B4BB8),
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
    Button(onClick = onClick, shape = RoundedCornerShape(12.dp)) {
        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(4.dp))
        Text("Agregar")
    }
}

@Composable
private fun EmptyState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = null,
            tint = Color(0xFFC9B8F0),
            modifier = Modifier.size(120.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No hay productos",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF1E1747)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Agrega tu primer producto para comenzar.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF5B5280),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        AddButton(onClick = onAddClick)
    }
}

@Composable
private fun ProductItem(product: Product, onClick: () -> Unit) {
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
                    .background(Color(0xFFE9DFFB), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null,
                    tint = Color(0xFF6B4BB8)
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
                    text = "Stock: ${product.existencias} • \$%.2f".format(product.precio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5B5280)
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