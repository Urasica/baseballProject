package com.example.baseballapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.saveToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var loginService: LoginService
    private lateinit var buttonSignUp: Button

    private val TAG = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        buttonSignUp = findViewById(R.id.buttonSignUp)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        loginService = LoginService(this)

        buttonSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val username = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()

        val client = OkHttpClient()
        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }
        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://35.216.0.159:8080/auth/login")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Login failed", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, "Response Code: ${response.code}")
                Log.d(TAG, "Response Body: $responseBody")

                if (response.isSuccessful) {
                    val json = JSONObject(responseBody)
                    val token = json.getString("token")

                    // JWT 토큰을 SharedPreferences에 저장하는 코드
                    saveToken(this@LoginActivity, token)

                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "$username 로그인 성공하셨습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "Invalid username or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
}