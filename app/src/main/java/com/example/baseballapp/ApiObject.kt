package com.example.baseballapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiObject {
    private const val BASE_URL="http://35.216.0.159:8080/api/"

    private val getRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getRetrofitService:UpbitAPI by lazy {
        getRetrofit.create(UpbitAPI::class.java)
    }

}