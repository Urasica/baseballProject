package com.example.login

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    // 토큰을 저장하고 관리하는 클래스. 각 기능에서 토큰을 호출하거나 저장할 때 사용하면 됨

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearToken() {
        with(sharedPreferences.edit()) {
            remove("auth_token")
            apply()
        }
    }
}
