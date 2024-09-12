package com.example.baseballapp

import android.content.Context
import com.example.login.TokenManager
import okhttp3.*
import java.io.IOException

class LoginService(private val context: Context) {

    private val tokenManager = TokenManager(context)
    private val client = OkHttpClient()

    // 토큰이 정상적인지 확인하는 기능. 이 함수는 Boolean 값만 반환, 처리는 메인 액티비티의 코드 참조
    fun checkToken(callback: (Boolean) -> Unit) {
        val token = tokenManager.getToken()

        if (token != null) {
            val request = Request.Builder()
                .url("http://35.216.0.159:8080/auth/check-token")
                .header("Authorization", "Bearer $token")
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback(response.isSuccessful)
                }
            })
        } else {
            callback(false)
        }
    }
    fun getUsername(): String? {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }
}
