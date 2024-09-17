package com.example.login

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearToken() {
        with(sharedPreferences.edit()) {
            remove("auth_token")
            apply()
        }
    }


    fun getUsername(): String? {
        return sharedPreferences.getString("username", null) // 저장된 사용자 이름 반환
    }

    // 사용자 이름 저장 메서드
    fun saveUsername(username: String) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            apply()
        }
    }
}
