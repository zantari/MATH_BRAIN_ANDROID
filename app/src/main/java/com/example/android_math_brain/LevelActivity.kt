package com.example.android_math_brain

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.ImageView

class LevelActivity : AppCompatActivity() {
    private lateinit var gameData: GameData
    private var wrongAnswers = 0;
    private var levelId = -1
    private var currQuestionIndex = 0;
    private lateinit var currentLevelQuestion: MutableList<Question>

    private lateinit var btn1: Button;
    private lateinit var btn2: Button;
    private lateinit var btn3: Button
    private lateinit var excText: TextView;

    private lateinit var progressBar: SeekBar
    private lateinit var btnReturn: ImageView

//    PYTANIA
    private val lvl1 = listOf(
        Question("2 + 2 = ?", listOf("3", "8", "4"),  2),

        Question("4 + 4 = ?", listOf("8", "13", "21"),  0),

        Question("3 + 6 = ?", listOf("8", "9", "10"),  1)
        //ten poziom ma 3 etapy wiec tylko 3 razy Question
    )

    private val lvl2 = listOf(
        Question("8 + 8 = ?", listOf("12", "16", "27"),  1),

        Question("14 + 3 = ?", listOf("17", "13", "21"),  0),

        Question("21 + 8 = ?", listOf("26", "23", "29"),  2),

        Question("7 + 3 = ?", listOf("10", "3", "9"),  0),

        Question("11 + 3 = ?", listOf("14", "13", "15"),  0)
        //ten poziom ma 5 etapy wiec 5 razy Question
    )






















    private val levelSequence = listOf(lvl1, lvl2) //JAK DODASZ POZIOM PRIVATE VAL LVL.. TO DODAJ GO TU!!!


    override fun onCreate(savedInstanceState: Bundle?) {

        gameData = GameData.getInstance(this)

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
            showToast(this, "WE DONT HAVE THIS LEVEL YET")
            finish()
            return
        }

         btn1 = findViewById<Button>(R.id.answer1)
         btn2 = findViewById<Button>(R.id.answer2)
         btn3 = findViewById<Button>(R.id.answer3)
        btnReturn = findViewById(R.id.btnReturn)

         excText = findViewById<TextView>(R.id.excText)
        progressBar = findViewById(R.id.progressBar)
        progressBar.setOnTouchListener { v, event -> true }
        currentLevelQuestion = levelSequence[levelId-1].toMutableList()

        btn1.setOnClickListener { checkAnswer(0) }
        btn2.setOnClickListener { checkAnswer(1) }
        btn3.setOnClickListener { checkAnswer(2) }
        loadQuestion()

        btnReturn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            Intent(this, AdventureActivity::class.java).also {
                startActivity(it)
                finish()
            }

        }







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
        VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)


        val currentQuestion = currentLevelQuestion[currQuestionIndex]

        if (selectedAnswerIndex == currentQuestion.correctAnswerIndex) {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.CORRECT)
            showToast(this, "correct answer!")
            currQuestionIndex++
            if (currQuestionIndex < currentLevelQuestion.size) {
                loadQuestion()
            } else {
                showToast(this, "you ended up with " + wrongAnswers +" wrong answers")



                val intent = Intent(this, EndLevelActivity::class.java)
                val procenty = (currentLevelQuestion.size - wrongAnswers) * 100 / currentLevelQuestion.size
                val isWon = procenty>32

                if(isWon){
                    if(levelId == gameData.getLevel()) {
                        gameData.addLevel()
                    }
                }

                intent.putExtra("all_answers", currentLevelQuestion.size.toInt())
                intent.putExtra("passed_level", levelId)
                intent.putExtra("wrong_answers", wrongAnswers)
                intent.putExtra("procenty", procenty)
                intent.putExtra("isWon", isWon)

                startActivity(intent)
                finish()
                return

            }





        }
        else{
            VibrationManager.vibrate(this, VibrationManager.VibrationType.WRONG)
            wrongAnswers++
            showToast(this, "wrong answer correct is: "+ currentQuestion.answers[currentQuestion.correctAnswerIndex].toString())
            currentLevelQuestion.removeAt(currQuestionIndex)
            currentLevelQuestion.add(currentQuestion)
            loadQuestion()
        }

    }




}


