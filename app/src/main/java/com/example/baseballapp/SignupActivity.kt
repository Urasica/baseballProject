package com.example.baseballapp

import SignupRequest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baseballapp.R
import com.example.login.AuthService
import com.example.login.SignupResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var signupButton: Button

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://35.216.0.159:8080/auth/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService = retrofit.create(AuthService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        nameEditText = findViewById(R.id.nameEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        signupButton = findViewById(R.id.signupButton)

        signupButton.setOnClickListener {
            performSignup()
        }
    }

    private fun performSignup() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val name = nameEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()

        val signupRequest = SignupRequest(username, password, name, phoneNumber)

        authService.signup(signupRequest).enqueue(object : Callback<SignupResponse> {

            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                Log.d("SignupActivity", "Response raw: ${response.raw()}")
                Log.d("SignupActivity", "Response code: ${response.code()}")
                Log.d("SignupActivity", "Response message: ${response.message()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("SignupActivity", "Response body: ${responseBody?.message}")
                    Toast.makeText(this@SignupActivity, responseBody?.message, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SignupActivity", "Error body: $errorBody")
                    Toast.makeText(this@SignupActivity, errorBody, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}