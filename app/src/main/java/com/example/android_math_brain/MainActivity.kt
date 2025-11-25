package com.example.android_math_brain

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var gameData: GameData


    override fun onCreate(savedInstanceState: Bundle?) {

        gameData = GameData.getInstance(this)



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val advBtn = findViewById<Button>(R.id.adventureBtn);
        val rankBtn = findViewById<Button>(R.id.rankedBtn);
        val adventureScoreText = findViewById<TextView>(R.id.adventureScore);

        adventureScoreText.text = "\uD83D\uDDFA\uFE0F: " + gameData.getLevel().toString()




        advBtn.setOnClickListener {
            gameData.setLevel(2)
            Intent(this, AdventureActivity::class.java).also {
                startActivity(it)
            }
        }

    }


}