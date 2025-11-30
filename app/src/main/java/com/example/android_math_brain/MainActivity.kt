package com.example.android_math_brain

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

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
        val rankedScoreText = findViewById<TextView>(R.id.rankedScore);
        val settingsBtn = findViewById<ImageButton>(R.id.btnSettingsMain)


        adventureScoreText.text = "\uD83D\uDDFA\uFE0F" + "Current lvl: " + gameData.getLevel().toString()
        rankedScoreText.text = "\uD83C\uDFC6: " + gameData.getPoints().toString()


        settingsBtn.setOnClickListener {
            lifecycleScope.launch {
                adventureScoreText.text = "lvl = 0"

                gameData.setLevel(1)
                delay(500)
                onResume()

                adventureScoreText.text = "\uD83D\uDDFA\uFE0F" + "Current lvl: " + gameData.getLevel().toString()
            }

        }






        advBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)

            Intent(this, AdventureActivity::class.java).also {
                startActivity(it)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // lub top i bottom

            }
        }

        rankBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)


            Intent(this, RankedActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }


    }


    override fun onResume() {
        super.onResume()


        val adventureScoreText = findViewById<TextView>(R.id.adventureScore);


        adventureScoreText.text = "\uD83D\uDDFA\uFE0F: " + gameData.getLevel().toString()
    }


}