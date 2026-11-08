package com.hann.shopu3.StationeryInventory.Presentacion.Pantallas.Pages


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.AppBackground
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.AuthTextField
import com.hann.shopu3.StationeryInventory.Presentacion.Componentes.StatusBarIcons
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

    StatusBarIcons(darkIcons = true)

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
    AppBackground {
        Scaffold(containerColor = Color.Transparent) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
            ) {
                // Logo provisional: círculo morado con ícono
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(Color(0xFF6B4BB8), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Inventario Papelería",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF1E1747),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Crea tu cuenta para continuar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF5B5280),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                AuthTextField(
                    value = uiState.nombre,
                    onValueChange = onNombreChange,
                    label = "Nombre completo",
                    error = uiState.nombreError,
                    leadingIcon = Icons.Filled.Person
                )
                AuthTextField(
                    value = uiState.correo,
                    onValueChange = onCorreoChange,
                    label = "Correo electrónico",
                    error = uiState.correoError,
                    keyboardType = KeyboardType.Email,
                    leadingIcon = Icons.Filled.Email
                )
                AuthTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = "Contraseña",
                    error = uiState.passwordError,
                    isPassword = true,
                    leadingIcon = Icons.Filled.Lock
                )
                AuthTextField(
                    value = uiState.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = "Confirmar contraseña",
                    error = uiState.confirmPasswordError,
                    isPassword = true,
                    leadingIcon = Icons.Filled.Lock
                )

                uiState.errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = onRegister,
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Registrarse")
                    }
                }

                TextButton(onClick = onGoToLogin, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Color(0xFF5B5280))) {
                                append("¿Ya tienes cuenta? ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = Color(0xFF6B4BB8),
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Inicia sesión")
                            }
                        }
                    )
                }
            }
        }
    }
}