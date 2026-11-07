package com.hann.shopu3.StationeryInventory.Presentacion.pantallas

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hann.shopu3.StationeryInventory.Datos.modelo.Product
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.ErrorState
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.ProductListUiState
import com.hann.shopu3.StationeryInventory.Presentacion.viewmodels.ProductListViewModel
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

@OptIn(ExperimentalMaterial3Api::class)
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
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                actions = {
                    TextButton(onClick = onLogout) { Text("Cerrar sesión") }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nuevo") }
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

                uiState.products.isEmpty() -> Text(
                    text = "Todavía no hay productos. Toca «Nuevo» para agregar el primero.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )

                else -> LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.products, key = { it.id }) { product ->
                        ProductItem(product = product, onClick = { onProductClick(product.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Código: ${product.codigo}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("\$%.2f".format(product.precio), style = MaterialTheme.typography.bodyLarge)
                Text("Existencias: ${product.existencias}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}