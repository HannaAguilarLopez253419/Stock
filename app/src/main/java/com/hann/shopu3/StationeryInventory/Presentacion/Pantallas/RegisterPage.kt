package com.hann.shopu3.StationeryInventory.Presentacion.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.RegisterUiState
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.RegisterViewModel
import com.hann.shopu3.StationeryInventory.ShopApplication

@Composable
fun RegisterPage(
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as ShopApplication)
            .container.authViewModelFactory
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.onNavigated()
            onRegisterSuccess()
        }
    }

    RegisterContent(
        uiState = uiState,
        onNombreChange = viewModel::onNombreChange,
        onCorreoChange = viewModel::onCorreoChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onRegister = viewModel::register,
        onGoToLogin = onGoToLogin
    )
}

@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onGoToLogin: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium)

            AuthTextField(uiState.nombre, onNombreChange, "Nombre", error = uiState.nombreError)
            AuthTextField(
                uiState.correo, onCorreoChange, "Correo",
                error = uiState.correoError, keyboardType = KeyboardType.Email
            )
            AuthTextField(
                uiState.password, onPasswordChange, "Contraseña",
                error = uiState.passwordError, isPassword = true
            )
            AuthTextField(
                uiState.confirmPassword, onConfirmPasswordChange, "Confirmar contraseña",
                error = uiState.confirmPasswordError, isPassword = true
            )

            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = onRegister,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Registrarme")
                }
            }

            TextButton(onClick = onGoToLogin, modifier = Modifier.fillMaxWidth()) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}