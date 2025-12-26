package com.example.android_math_brain

import android.annotation.SuppressLint
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
import android.os.Handler
import android.os.Looper
import androidx.compose.ui.graphics.Color
import android.media.SoundPool

class LevelActivity : AppCompatActivity() {
    private lateinit var gameData: GameData
    private var wrongAnswers = 0
    private var levelId = -1
    private var currQuestionIndex = 0
    private lateinit var currentLevelQuestion: MutableList<Question>

    private lateinit var isGoodText: TextView
    private lateinit var correctAnswer: TextView
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var excText: TextView

    private lateinit var progressBar: SeekBar
    private lateinit var btnReturn: ImageView

    private lateinit var soundPool: SoundPool
    private var badSoundId: Int = 0
    private var goodSoundId: Int = 0
    private var soundReady = false

    private val positiveText = listOf<String>(
        "Awesome!", "Excellent!", "Great job!", "Perfect!", "You got it!",
        "Super!", "That's right!", "Unstoppable!", "You are the real math brain!"
    )

    private val negativeText = listOf(
        "Try again!", "Oops!", "Almost there!", "Keep trying!",
        "Incorrect.", "Better luck next time!", "A small miscalculation"
    )

    private val lvl1 = listOf(
        Question("2 + 2 = ", listOf("3", "8", "4"),  2),
        Question("4 + 4 = ", listOf("8", "6", "7"),  0),
        Question("3 + 6 = ", listOf("4", "9", "2"),  1)
    )
    private val lvl2 = listOf(
        Question("8 + 8 = ", listOf("12", "16", "27"),  1),
        Question("14 + 3 = ", listOf("17", "13", "21"),  0),
        Question("21 + 8 = ", listOf("26", "23", "29"),  2),
        Question("7 + 3 = ", listOf("10", "13", "11"),  0),
        Question("11 + 3 = ", listOf("14", "11", "15"),  0)
    )
    private val lvl3 = listOf(
        Question("12 + 8 = ", listOf("12", "26", "20"),  2),
        Question("13 + 5 = ", listOf("18", "19", "21"),  0),
        Question("18 + 1 = ", listOf("19", "20", "21"),  0),
        Question("24 + 8 = ", listOf("30", "42", "32"),  2),
        Question("31 + 3 = ", listOf("32", "34", "38"),  1),
        Question("32 + 13 = ", listOf("43", "45", "37"),  1),
        Question("39 + 28 = ", listOf("67", "64", "72"),  0)
    )
    private val lvl4 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))
    private val lvl5 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))
    private val lvl6 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))
    private val lvl7 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))
    private val lvl8 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))
    private val lvl9 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))
    private val lvl10 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))
    private val lvl11 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))
    private val lvl12 = listOf(Question("12 + 8 = ", listOf("12", "26", "20"), 2), Question("13 + 5 = ", listOf("18", "19", "21"), 0))

    private lateinit var buttons: List<Button>
    private lateinit var currLevel: TextView
    private val levelSequence = listOf(lvl1, lvl2, lvl3, lvl4, lvl5, lvl6, lvl7, lvl8, lvl9, lvl10, lvl11, lvl12)

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
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

        levelId = intent.getIntExtra("LEVEL_ID", -1)
        if(levelId == -1 || levelId > levelSequence.size){
            showToast(this, "WE DONT HAVE THIS LEVEL YET SORRY!")
            finish()
            return
        }

        currLevel = findViewById(R.id.currLvl)
        currLevel.text  = "level: $levelId"

        btn1 = findViewById(R.id.answer1)
        btn2 = findViewById(R.id.answer2)
        btn3 = findViewById(R.id.answer3)
        buttons = listOf(btn1, btn2, btn3)
        btnReturn = findViewById(R.id.btnReturn)
        isGoodText = findViewById(R.id.isGoodText)
        excText = findViewById(R.id.excText)
        progressBar = findViewById(R.id.progressBar)
        progressBar.setOnTouchListener { _, _ -> true }

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
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
            }
        }

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(
                android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_GAME)
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            ).build()

        badSoundId = soundPool.load(this, R.raw.wronganswer3, 1)
        goodSoundId = soundPool.load(this, R.raw.goodanswersound, 1)

        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) soundReady = true
        }
    }

    private fun loadQuestion(){
        val currentQuestion = currentLevelQuestion[currQuestionIndex]
        progressBar.max = currentLevelQuestion.size - 1
        progressBar.setProgress(currQuestionIndex, true)
        excText.text = currentQuestion.questionText + "?"
        btn1.text = currentQuestion.answers[0]
        btn2.text = currentQuestion.answers[1]
        btn3.text = currentQuestion.answers[2]
    }

    private var indexBtn = 0
    private fun checkAnswer(selectedAnswerIndex: Int) {


        indexBtn = selectedAnswerIndex
        val currentQuestion = currentLevelQuestion[currQuestionIndex]

        if (selectedAnswerIndex == currentQuestion.correctAnswerIndex) {
            if (soundReady) soundPool.play(goodSoundId, 1f, 1f, 1, 0, 1f)

            VibrationManager.vibrate(this, VibrationManager.VibrationType.CORRECT)
            val randomMessage = positiveText.random()
            showToast(this, "correct answer!")
            isGoodText.text = randomMessage
            isGoodText.setTextColor(android.graphics.Color.parseColor("#3FD054"))
            excText.setTextColor(android.graphics.Color.parseColor("#3FD054"))
            buttons[currentQuestion.correctAnswerIndex].setBackgroundColor(android.graphics.Color.parseColor("#3FD054"))
            buttons.forEach { it.isClickable = false }
            excText.text = currentQuestion.questionText + currentQuestion.answers[currentQuestion.correctAnswerIndex]

            Handler(Looper.getMainLooper()).postDelayed({
                excText.setTextColor(android.graphics.Color.parseColor("#1a3d59"))
                buttons.forEach {
                    it.isClickable = true
                    it.setBackgroundColor(android.graphics.Color.parseColor("#99c1f1"))
                }
                isGoodText.text = ""
                currQuestionIndex++

                if (currQuestionIndex < currentLevelQuestion.size) {
                    loadQuestion()
                } else {
                    showToast(this, "you ended up with $wrongAnswers wrong answers")
                    val intent = Intent(this, EndLevelActivity::class.java)
                    val procenty = (currentLevelQuestion.size - wrongAnswers) * 100 / currentLevelQuestion.size
                    val isWon = procenty > 32

                    if(isWon && levelId == gameData.getLevel()) {
                        gameData.addLevel()
                    }

                    intent.putExtra("all_answers", currentLevelQuestion.size)
                    intent.putExtra("passed_level", levelId)
                    intent.putExtra("wrong_answers", wrongAnswers)
                    intent.putExtra("procenty", procenty)
                    intent.putExtra("isWon", isWon)

                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
                    finish()
                }
            }, 1000)
        } else {
            if (soundReady) soundPool.play(badSoundId, 1f, 1f, 1, 0, 1f)

            VibrationManager.vibrate(this, VibrationManager.VibrationType.WRONG)
            wrongAnswers++

            isGoodText.setTextColor(android.graphics.Color.parseColor("#BA3030"))
            excText.setTextColor(android.graphics.Color.parseColor("#BA3030"))
            buttons[currentQuestion.correctAnswerIndex].setBackgroundColor(android.graphics.Color.parseColor("#3FD054"))
            buttons[indexBtn].setBackgroundColor(android.graphics.Color.parseColor("#BA3030"))
            buttons.forEach { it.isClickable = false }

            excText.text = currentQuestion.questionText + currentQuestion.answers[currentQuestion.correctAnswerIndex]
            isGoodText.text = negativeText.random()
            showToast(this, "wrong answer correct is: ${currentQuestion.answers[currentQuestion.correctAnswerIndex]}")

            Handler(Looper.getMainLooper()).postDelayed({
                buttons.forEach {
                    it.setBackgroundColor(android.graphics.Color.parseColor("#99c1f1"))
                    it.isClickable = true
                }
                excText.setTextColor(android.graphics.Color.parseColor("#1a3d59"))
                currentLevelQuestion.removeAt(currQuestionIndex)
                currentLevelQuestion.add(currentQuestion)
                loadQuestion()
                isGoodText.text = ""
            }, 1500)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}

fun Color.Companion.parseColor(string: String) {}