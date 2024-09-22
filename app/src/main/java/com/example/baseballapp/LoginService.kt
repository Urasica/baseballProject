package com.example.baseballapp

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
                    if (response.isSuccessful) {
                        callback(true)
                    } else {
                        // 토큰 유효성 검사가 실패하면 로그아웃 처리
                        logoutUser()
                        callback(false)
                    }
                }
            })
        } else {
            callback(false)
        }
    }

    // 로그아웃 처리 함수
    private fun logoutUser() {
        // 토큰 삭제
        tokenManager.clearToken()

        // 로그인 화면으로 이동
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

        // 로그아웃 메시지 표시
        Toast.makeText(context, "토큰이 만료되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
    }

    fun getUsername(): String? {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }
}
