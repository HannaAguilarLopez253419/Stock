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
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.LoginUiState
import com.hann.shopu3.StationeryInventory.Presentacion.Viewmodels.LoginViewModel
import com.hann.shopu3.StationeryInventory.ShopApplication

@Composable
fun LoginPage(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as ShopApplication)
            .container.authViewModelFactory
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.onNavigated()
            onLoginSuccess()
        }
    }

    LoginContent(
        uiState = uiState,
        onCorreoChange = viewModel::onCorreoChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogin = viewModel::login,
        onGoToRegister = onGoToRegister
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onCorreoChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onGoToRegister: () -> Unit
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
            Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)

            AuthTextField(
                value = uiState.correo,
                onValueChange = onCorreoChange,
                label = "Correo",
                error = uiState.correoError,
                keyboardType = KeyboardType.Email
            )
            AuthTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                error = uiState.passwordError,
                isPassword = true
            )

            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = onLogin,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Entrar")
                }
            }

            TextButton(onClick = onGoToRegister, modifier = Modifier.fillMaxWidth()) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}