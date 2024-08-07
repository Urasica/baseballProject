package com.example.baseballapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.baseballapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import RankingFragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.yourapp.CommunityFragment
import androidx.appcompat.app.ActionBarDrawerToggle

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

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

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, ScheduleFragment())
            .commit()

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

        val headerView = navView.getHeaderView(0)
        val profileImage: ImageView = headerView.findViewById(R.id.profile_image)
        val userName: TextView = headerView.findViewById(R.id.user_name)
        val userEmail: TextView = headerView.findViewById(R.id.user_email)
        val logoutButton: Button = headerView.findViewById(R.id.logout_button)

        userName.text = "Yoonsojoung"
        userEmail.text = "Yoonsojoung@naver.com"

        logoutButton.setOnClickListener {
            handleLogout()
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

    private fun handleLogout() {
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            super.onBackPressed()
        }
    }
}