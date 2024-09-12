package com.example.login

import LoginRequest
import LoginResponse
import SignupRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>


    @POST("/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/auth/check-token")
    fun checkToken(@Header("Authorization") token: String): Call<String>
}

data class SignupResponse(
    val message: String
)