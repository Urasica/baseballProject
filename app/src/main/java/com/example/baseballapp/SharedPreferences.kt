package com.example.login

import android.content.Context
import android.content.SharedPreferences

fun saveToken(context: Context, token: String) {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("auth_token", token)
        apply()
    }
}