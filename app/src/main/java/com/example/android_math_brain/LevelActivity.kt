package com.example.android_math_brain

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent

class LevelActivity : AppCompatActivity() {
    private var levelId = -1
    private var currQuestionIndex = 0;
    private lateinit var currentLevelQuestion: List<Question>

    private lateinit var btn1: Button;
    private lateinit var btn2: Button;
    private lateinit var btn3: Button
    private lateinit var excText: TextView;

    private lateinit var progressBar: SeekBar

//    PYTANIA
    private val lvl1 = listOf(
        Question("2 + 2 = ?", listOf("3", "8", "4"),  2),

        Question("8 + 8 = ?", listOf("16", "13", "21"),  0)
        //ten poziom ma 2 etapy wiec tylko 2 razy Question
    )

    private val lvl2 = listOf(
        Question("lvl 2 ?", listOf("-4", "8", "67"),  2),

        Question("8 + 8 = ?", listOf("16", "13", "21"),  0),
        Question("3ETAP", listOf("16", "13", "21"),  0)
        //ten poziom ma 3 etapy wiec 3 razy Question
    )




















    private val levelSequence = listOf(lvl1, lvl2) //JAK DODASZ POZIOM PRIVATE VAL LVL.. TO DODAJ GO TU!!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //test
        levelId = intent.getIntExtra("LEVEL_ID", -1)

        if(levelId == -1 || levelId > levelSequence.size){
            finish()
            return
        }

         btn1 = findViewById<Button>(R.id.answer1)
         btn2 = findViewById<Button>(R.id.answer2)
         btn3 = findViewById<Button>(R.id.answer3)

         excText = findViewById<TextView>(R.id.excText)
        progressBar = findViewById(R.id.progressBar)
        progressBar.setOnTouchListener { v, event -> true }
        currentLevelQuestion = levelSequence[levelId-1]

        btn1.setOnClickListener { checkAnswer(0) }
        btn2.setOnClickListener { checkAnswer(1) }
        btn3.setOnClickListener { checkAnswer(2) }
        loadQuestion()






    }

    private fun loadQuestion(){
        val currentQuestion = currentLevelQuestion[currQuestionIndex]

        progressBar.max = currentLevelQuestion.size - 1
        progressBar.progress = currQuestionIndex


        excText.text = currentQuestion.questionText
        btn1.text = currentQuestion.answers[0]
        btn2.text = currentQuestion.answers[1]
        btn3.text = currentQuestion.answers[2]


    }

    private fun checkAnswer(selectedAnswerIndex: Int) {
        val currentQuestion = currentLevelQuestion[currQuestionIndex]
        if (selectedAnswerIndex == currentQuestion.correctAnswerIndex) {
            currQuestionIndex++
            if (currQuestionIndex < currentLevelQuestion.size) {
                loadQuestion()
            } else {
                Toast.makeText(this, "WYGRALES! ", Toast.LENGTH_SHORT).show()
                Intent(this, EndLevelActivity::class.java).also {
                    startActivity(it)
                }
            }





        }
        else{
            Toast.makeText(this, "zla odpowiedz ", Toast.LENGTH_LONG).show()
        }

    }




}


