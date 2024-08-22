package com.example.baseballapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // XML에서 정의된 뷰 요소를 가져오기
        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val signUpTextView = findViewById<TextView>(R.id.signUpTextView)

        // 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(username, password)) {
                // 로그인 성공 시 MainActivity로 이동
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // 로그인 실패 시 처리 (예: 토스트 메시지 등)
            }
        }

        // 회원가입 텍스트 클릭 리스너 (추가 기능 구현 가능)
        signUpTextView.setOnClickListener {
            // 회원가입 화면으로 이동하는 코드 추가 가능
        }
    }

    private fun validateLogin(username: String, password: String): Boolean {
        // 임시로 모든 로그인 시도 성공으로 처리
        return true
    }
}
