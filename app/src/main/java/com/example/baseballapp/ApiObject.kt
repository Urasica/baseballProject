package com.example.baseballapp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiObject {
    private const val BASE_URL = "http://35.216.0.159:8080/api/"
    private var token: String? = null

    // 사용자 토큰을 설정하는 메서드
    fun setToken(authToken: String) {
        token = authToken
    }

    // 요청에 Authorization 헤더를 자동으로 추가하는 인터셉터
    private val authInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Content-Type", "application/json")

        // 토큰이 존재하면 Authorization 헤더에 추가
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    // OkHttpClient에 인터셉터 추가
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    // Retrofit 객체 생성
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // OkHttpClient를 Retrofit에 설정
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getRetrofitService: UpbitAPI by lazy {
        retrofit.create(UpbitAPI::class.java)
    }
}
