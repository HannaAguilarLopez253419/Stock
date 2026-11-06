package com.hann.shopu3.StationeryInventory.Datos.Fuentededatos

import com.hann.shopu3.StationeryInventory.Datos.dto.AuthResponseDto
import com.hann.shopu3.StationeryInventory.Datos.dto.LoginRequestDto
import com.hann.shopu3.StationeryInventory.Datos.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("register")
    suspend fun register(@Body body: RegisterRequestDto): AuthResponseDto

    @POST("login")
    suspend fun login(@Body body: LoginRequestDto): AuthResponseDto
}