package com.hann.shopu3.StationeryInventory.Presentacion.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.AuthTextField
import com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.ProductFormUiState
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.ProductFormViewModel
import com.hann.shopu3.StationeryInventory.ShopApplication

@Composable
fun ProductFormPage(
    productId: Int?,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: ProductFormViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as ShopApplication)
            .container.productViewModelFactory(productId)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) onSessionExpired()
    }

    ProductFormContent(
        uiState = uiState,
        onBack = onBack,
        onNombreChange = viewModel::onNombreChange,
        onCodigoChange = viewModel::onCodigoChange,
        onPrecioChange = viewModel::onPrecioChange,
        onExistenciasChange = viewModel::onExistenciasChange,
        onCategoriaChange = viewModel::onCategoriaChange,
        onFechaIngresoChange = viewModel::onFechaIngresoChange,
        onSave = viewModel::save
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormContent(
    uiState: ProductFormUiState,
    onBack: () -> Unit,
    onNombreChange: (String) -> Unit,
    onCodigoChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onExistenciasChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onFechaIngresoChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isEditing) "Editar producto" else "Nuevo producto")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AuthTextField(
                    value = uiState.nombre,
                    onValueChange = onNombreChange,
                    label = "Nombre",
                    error = uiState.nombreError
                )
                AuthTextField(
                    value = uiState.codigo,
                    onValueChange = onCodigoChange,
                    label = "Código",
                    error = uiState.codigoError
                )
                AuthTextField(
                    value = uiState.precio,
                    onValueChange = onPrecioChange,
                    label = "Precio",
                    error = uiState.precioError,
                    keyboardType = KeyboardType.Decimal
                )
                AuthTextField(
                    value = uiState.existencias,
                    onValueChange = onExistenciasChange,
                    label = "Existencias",
                    error = uiState.existenciasError,
                    keyboardType = KeyboardType.Number
                )
                AuthTextField(
                    value = uiState.categoria,
                    onValueChange = onCategoriaChange,
                    label = "Categoría (opcional)"
                )
                AuthTextField(
                    value = uiState.fechaIngreso,
                    onValueChange = onFechaIngresoChange,
                    label = "Fecha de ingreso (AAAA-MM-DD)",
                    error = uiState.fechaIngresoError
                )

                uiState.errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = onSave,
                    enabled = !uiState.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}