package com.example.baseballapp

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class Metaverse2Activity : AppCompatActivity() {

    private lateinit var character: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metaverse2)

        character = findViewById(R.id.character)

        Glide.with(this).asGif().load(R.drawable.standing).into(character)
    }
}