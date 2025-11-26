package com.example.android_math_brain

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EndLevelActivity : AppCompatActivity() {

    private var allAnswers = -1;
    private var passedLevel = -1;
    private var wrongAnswers = -1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_end_level)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        passedLevel = intent.getIntExtra("passed_level", -1)
        allAnswers = intent.getIntExtra("all_answers", -1)
        wrongAnswers = intent.getIntExtra("wrong_answers", -1)

        if(passedLevel == -1 || allAnswers == -1 || wrongAnswers == -1){
            finish()
            return
        }

        Log.d("EndLevelActivity", "Passed level: $passedLevel")
        Log.d("EndLevelActivity", "All answers: $allAnswers")
        Log.d("EndLevelActivity", "Wrong answers: $wrongAnswers")





    }
}