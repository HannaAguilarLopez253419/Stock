package com.hann.shopu3.StationeryInventory.Datos.mapper
import com.hann.shopu3.StationeryInventory.Datos.dto.UserDto
import com.hann.shopu3.StationeryInventory.Datos.modelo.User

fun UserDto.toDomain(): User = User(
    id = id,
    nombre = nombre,
    correo = correo
)