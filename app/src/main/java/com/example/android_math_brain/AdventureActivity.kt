package com.example.android_math_brain

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView

class AdventureActivity : AppCompatActivity() {
    private lateinit var gameData: GameData // nie usuwaj

    override fun onCreate(savedInstanceState: Bundle?) {
        gameData = GameData.getInstance(this) //nie usuwaj



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adventure)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val scrollView = findViewById<ScrollView>(R.id.scrollableContent)
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }







        //mozesz wyjebac pod spodem

        val buttonPowrot = findViewById<ImageButton>(R.id.goBack);



        buttonPowrot.setOnClickListener {

            finish()
        }


    }
}