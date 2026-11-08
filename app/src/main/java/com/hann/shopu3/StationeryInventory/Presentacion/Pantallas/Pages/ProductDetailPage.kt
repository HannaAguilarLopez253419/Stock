package com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.Pages


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.AppTopBar
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.ErrorState
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.StatusBarIcons
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.States.ProductDetailUiState
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.ProductDetailViewModel
import com.hann.shopu3.StationeryInventory.ShopApplication

@Composable
fun ProductDetailPage(
    productId: Int,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    onDeleted: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: ProductDetailViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as ShopApplication)
            .container.productViewModelFactory(productId)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    StatusBarIcons(darkIcons = false)

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.loadProduct() }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) onDeleted()
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) onSessionExpired()
    }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    ProductDetailContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onEdit = { onEdit(productId) },
        onDeleteClick = viewModel::onDeleteClick,
        onDismissDelete = viewModel::onDismissDelete,
        onConfirmDelete = viewModel::confirmDelete,
        onRetry = viewModel::loadProduct
    )
}

@Composable
fun ProductDetailContent(
    uiState: ProductDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
    onRetry: () -> Unit
) {
    val product = uiState.product

    Scaffold(
        containerColor = Color(0xFFFAF7FF),
        topBar = { AppTopBar(title = "Detalle del Producto", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                product != null -> Column(
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = product.nombre,
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color(0xFF4B2E8F),
                                textAlign = TextAlign.Center
                            )
                            DetailLine("Código: ${product.codigo}")
                            DetailLine("Stock: ${product.existencias}")
                            DetailLine("Precio: \$%.2f".format(product.precio))
                            DetailLine("Categoría: ${product.categoria ?: "Sin categoría"}")
                        }
                    }

                    Button(
                        onClick = onEdit,
                        enabled = !uiState.isDeleting,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Editar")
                    }

                    OutlinedButton(
                        onClick = onDeleteClick,
                        enabled = !uiState.isDeleting,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFFDECEC),
                            contentColor = Color(0xFFD32F2F)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Eliminar")
                    }
                }

                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                uiState.errorMessage != null -> ErrorState(
                    message = uiState.errorMessage.orEmpty(),
                    onRetry = onRetry
                )
            }
        }
    }

    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = onDismissDelete,
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            icon = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFF6B4BB8), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            },
            title = {
                Text(
                    text = "¿Eliminar este producto?",
                    color = Color(0xFF1E1747),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Esta acción no se puede deshacer.",
                    color = Color(0xFF5B5280),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismissDelete,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF6B4BB8)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6B4BB8))
                ) { Text("Cancelar") }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmDelete,
                    enabled = !uiState.isDeleting,
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Eliminar") }
            }
        )
    }
}

@Composable
private fun DetailLine(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFF1E1747),
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}