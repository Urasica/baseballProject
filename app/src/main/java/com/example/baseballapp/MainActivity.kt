package com.example.baseballapp

import RankingFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.baseballapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import android.widget.Button
import android.widget.TextView
import com.example.login.LoginService
import com.example.login.TokenManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var loginService: LoginService
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar3)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        tokenManager = TokenManager(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, ScheduleFragment())
            .commit()

        loginService = LoginService(this)

        updateDrawerUI()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.ScheduleFragment -> ScheduleFragment()
                R.id.RankingFragment -> RankingFragment()
                R.id.InformationFragment -> InformationFragment()
                R.id.CommunityFragment -> CommunityFragment()
                R.id.ShopFragment -> ShopFragment()
                else -> null
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, it)
                    .commit()
                true
            } ?: false
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.ScheduleFragment -> ScheduleFragment()
                R.id.RankingFragment -> RankingFragment()
                R.id.InformationFragment -> InformationFragment()
                R.id.CommunityFragment -> CommunityFragment()
                R.id.ShopFragment -> ShopFragment()
                else -> null
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, it)
                    .commit()
                drawerLayout.closeDrawers()
                true
            } ?: false
        }
    }

    private fun updateDrawerUI() {
        val headerView = navView.getHeaderView(0)
        val loginButton: Button = headerView.findViewById(R.id.login_button)
        val userNameTextView: TextView = headerView.findViewById(R.id.user_name)

        loginService.checkToken { isLoggedIn ->
            runOnUiThread {
                if (isLoggedIn) {
                    val token = tokenManager.getToken()  // TokenManager 사용
                    if (token != null) {
                        // 로그인 상태
                        val username = loginService.getUsernameFromToken(token)  // 유저네임 추출
                        userNameTextView.text = username
                        loginButton.text = "Logout"
                        loginButton.setOnClickListener {
                            // 로그아웃 처리
                            tokenManager.clearToken()  // TokenManager 사용
                            Log.d("MainActivity", "Token cleared: ${tokenManager.getToken() == null}")  // 로그 찍기
                            updateDrawerUI() // 로그아웃 후 UI 업데이트
                        }
                    }
                } else {
                    // 로그아웃 상태
                    userNameTextView.text = "User Name"
                    loginButton.text = "Login"
                    loginButton.setOnClickListener {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            super.onBackPressed()
        }
    }
}