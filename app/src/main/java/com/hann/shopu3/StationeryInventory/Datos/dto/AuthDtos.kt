package com.hann.shopu3.StationeryInventory.Datos.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val nombre: String,
    val correo: String,
    val password: String
)

@Serializable
data class LoginRequestDto(
    val correo: String,
    val password: String
)

@Serializable
data class UserDto(
    val id: Int,
    val nombre: String,
    val correo: String
)

@Serializable
data class AuthResponseDto(
    val token: String,
    val usuario: UserDto
)

@Serializable
data class ErrorDto(
    val mensaje: String,
    val errores: List<String>? = null
)