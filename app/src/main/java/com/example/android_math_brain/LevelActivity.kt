package com.example.android_math_brain

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    private lateinit var btnReturn: ImageButton

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

    // DODAWANIE (1-10)
    // DODAWANIE (1-10)
    // DODAWANIE (1-10)
    private val lvl1 = listOf( // 3 pytania
        Question("2 + 5 = ", listOf("6", "7", "8"), 1),
        Question("4 + 4 = ", listOf("7", "8", "9"), 1),
        Question("1 + 8 = ", listOf("8", "9", "10"), 1)
    )
    private val lvl2 = listOf( // 4 pytania
        Question("10 + 5 = ", listOf("14", "15", "16"), 1),
        Question("2 + 3 + 4 = ", listOf("8", "9", "10"), 1),
        Question("20 + 7 = ", listOf("26", "27", "28"), 1),
        Question("5 + 5 + 5 = ", listOf("14", "15", "16"), 1)
    )
    private val lvl3 = listOf( // 3 pytania
        Question("30 + 9 = ", listOf("38", "39", "40"), 1),
        Question("8 + 1 + 8 = ", listOf("16", "17", "18"), 1),
        Question("40 + 6 = ", listOf("45", "46", "47"), 1)
    )
    private val lvl4 = listOf( // 5 pytań
        Question("50 + 8 = ", listOf("57", "58", "59"), 1),
        Question("9 + 9 + 2 = ", listOf("19", "20", "21"), 1),
        Question("60 + 5 = ", listOf("64", "65", "66"), 1),
        Question("7 + 7 + 7 = ", listOf("20", "21", "22"), 1),
        Question("12 + 7 = ", listOf("18", "19", "20"), 1)
    )
    private val lvl5 = listOf( // 4 pytania
        Question("80 + 4 = ", listOf("83", "84", "85"), 1),
        Question("4 + 4 + 4 + 4 = ", listOf("15", "16", "17"), 1),
        Question("15 + 5 = ", listOf("19", "20", "21"), 1),
        Question("6 + 6 + 6 = ", listOf("17", "18", "19"), 1)
    )
    private val lvl6 = listOf( // 6 pytań
        Question("90 + 9 = ", listOf("98", "99", "100"), 1),
        Question("2 + 2 + 2 + 2 + 2 = ", listOf("9", "10", "11"), 1),
        Question("25 + 5 = ", listOf("29", "30", "31"), 1),
        Question("8 + 8 + 1 = ", listOf("16", "17", "18"), 1),
        Question("70 + 10 = ", listOf("70", "80", "90"), 1),
        Question("1 + 1 + 1 + 1 + 1 + 1 = ", listOf("5", "6", "7"), 1)
    )
    private val lvl7 = listOf( // 3 pytania
        Question("45 + 5 = ", listOf("40", "50", "60"), 1),
        Question("9 + 1 + 9 = ", listOf("18", "19", "20"), 1),
        Question("88 + 2 = ", listOf("80", "90", "100"), 1)
    )
    private val lvl8 = listOf( // 5 pytań
        Question("33 + 7 = ", listOf("30", "40", "50"), 1),
        Question("5 + 5 + 5 + 5 = ", listOf("15", "20", "25"), 1),
        Question("65 + 5 = ", listOf("60", "70", "80"), 1),
        Question("3 + 3 + 3 + 3 = ", listOf("10", "12", "14"), 1),
        Question("19 + 1 = ", listOf("19", "20", "21"), 1)
    )
    private val lvl9 = listOf( // 4 pytania
        Question("75 + 5 = ", listOf("70", "80", "90"), 1),
        Question("8 + 2 + 8 + 2 = ", listOf("18", "20", "22"), 1),
        Question("50 + 50 = ", listOf("90", "100", "110"), 1),
        Question("4 + 4 + 4 + 4 + 4 = ", listOf("18", "20", "22"), 1)
    )
    private val lvl10 = listOf( // 6 pytań
        Question("95 + 5 = ", listOf("90", "100", "110"), 1),
        Question("9 + 9 + 9 = ", listOf("26", "27", "28"), 1),
        Question("40 + 30 = ", listOf("60", "70", "80"), 1),
        Question("2 + 2 + 2 + 2 + 2 + 2 = ", listOf("10", "12", "14"), 1),
        Question("15 + 15 = ", listOf("25", "30", "35"), 1),
        Question("10 - 3 = ", listOf("6", "7", "8"), 1)
    )

    // ODEJMOWANIE (11-20)
    private val lvl11 = listOf( // 3 pytania
        Question("10 - 4 = ", listOf("5", "6", "7"), 1),
        Question("20 - 5 = ", listOf("14", "15", "16"), 1),
        Question("15 - 8 = ", listOf("6", "7", "8"), 1)
    )
    private val lvl12 = listOf( // 4 pytania
        Question("30 - 10 = ", listOf("10", "20", "30"), 1),
        Question("9 - 2 - 2 = ", listOf("4", "5", "6"), 1),
        Question("25 - 5 = ", listOf("15", "20", "25"), 1),
        Question("8 - 1 - 1 = ", listOf("5", "6", "7"), 1)
    )
    private val lvl13 = listOf( // 3 pytania
        Question("50 - 20 = ", listOf("20", "30", "40"), 1),
        Question("7 - 2 - 1 = ", listOf("3", "4", "5"), 1),
        Question("100 - 50 = ", listOf("40", "50", "60"), 1)
    )
    private val lvl14 = listOf( // 5 pytań
        Question("80 - 40 = ", listOf("30", "40", "50"), 1),
        Question("9 - 3 - 3 = ", listOf("2", "3", "4"), 1),
        Question("60 - 10 = ", listOf("40", "50", "60"), 1),
        Question("5 - 1 - 1 - 1 = ", listOf("1", "2", "3"), 1),
        Question("45 - 5 = ", listOf("35", "40", "45"), 1)
    )
    private val lvl15 = listOf( // 4 pytania
        Question("75 - 25 = ", listOf("40", "50", "60"), 1),
        Question("8 - 4 - 2 = ", listOf("1", "2", "3"), 1),
        Question("90 - 30 = ", listOf("50", "60", "70"), 1),
        Question("6 - 1 - 1 = ", listOf("3", "4", "5"), 1)
    )
    private val lvl16 = listOf( // 6 pytań
        Question("100 - 20 = ", listOf("70", "80", "90"), 1),
        Question("9 - 1 - 1 - 1 - 1 = ", listOf("4", "5", "6"), 1),
        Question("55 - 5 = ", listOf("45", "50", "55"), 1),
        Question("4 - 1 - 1 = ", listOf("1", "2", "3"), 1),
        Question("100 - 75 = ", listOf("20", "25", "30"), 1),
        Question("7 - 3 - 1 = ", listOf("2", "3", "4"), 1)
    )
    private val lvl17 = listOf( // 3 pytania
        Question("88 - 8 = ", listOf("70", "80", "90"), 1),
        Question("9 - 4 - 4 = ", listOf("0", "1", "2"), 1),
        Question("100 - 1 = ", listOf("98", "99", "100"), 1)
    )
    private val lvl18 = listOf( // 5 pytań
        Question("66 - 6 = ", listOf("50", "60", "70"), 1),
        Question("5 - 1 - 1 = ", listOf("2", "3", "4"), 1),
        Question("40 - 15 = ", listOf("20", "25", "30"), 1),
        Question("8 - 2 - 2 - 2 = ", listOf("1", "2", "3"), 1),
        Question("100 - 10 = ", listOf("80", "90", "100"), 1)
    )
    private val lvl19 = listOf( // 4 pytania
        Question("99 - 9 = ", listOf("80", "90", "100"), 1),
        Question("6 - 2 - 2 = ", listOf("1", "2", "3"), 1),
        Question("70 - 35 = ", listOf("30", "35", "40"), 1),
        Question("9 - 5 - 1 = ", listOf("2", "3", "4"), 1)
    )
    private val lvl20 = listOf( // 6 pytań
        Question("100 - 50 = ", listOf("40", "50", "60"), 1),
        Question("4 - 1 - 1 - 1 = ", listOf("0", "1", "2"), 1),
        Question("85 - 15 = ", listOf("65", "70", "75"), 1),
        Question("8 - 1 - 1 - 1 = ", listOf("4", "5", "6"), 1),
        Question("50 - 25 = ", listOf("20", "25", "30"), 1),
        Question("2 * 3 = ", listOf("5", "6", "7"), 1)
    )

    // MNOŻENIE (21-30)
    private val lvl21 = listOf( // 3 pytania
        Question("2 * 4 = ", listOf("6", "8", "10"), 1),
        Question("3 * 3 = ", listOf("8", "9", "10"), 1),
        Question("5 * 2 = ", listOf("8", "10", "12"), 1)
    )
    private val lvl22 = listOf( // 4 pytania
        Question("6 * 2 = ", listOf("10", "12", "14"), 1),
        Question("2 * 2 * 2 = ", listOf("6", "8", "10"), 1),
        Question("4 * 3 = ", listOf("11", "12", "13"), 1),
        Question("3 * 1 * 3 = ", listOf("8", "9", "10"), 1)
    )
    private val lvl23 = listOf( // 3 pytania
        Question("10 * 4 = ", listOf("35", "40", "45"), 1),
        Question("2 * 5 * 2 = ", listOf("18", "20", "22"), 1),
        Question("7 * 2 = ", listOf("12", "14", "16"), 1)
    )
    private val lvl24 = listOf( // 5 pytań
        Question("5 * 5 = ", listOf("20", "25", "30"), 1),
        Question("2 * 2 * 3 = ", listOf("10", "12", "14"), 1),
        Question("8 * 3 = ", listOf("22", "24", "26"), 1),
        Question("4 * 1 * 4 = ", listOf("14", "16", "18"), 1),
        Question("10 * 8 = ", listOf("70", "80", "90"), 1)
    )
    private val lvl25 = listOf( // 4 pytania
        Question("6 * 3 = ", listOf("16", "18", "20"), 1),
        Question("3 * 3 * 3 = ", listOf("25", "27", "29"), 1),
        Question("9 * 2 = ", listOf("16", "18", "20"), 1),
        Question("2 * 2 * 4 = ", listOf("14", "16", "18"), 1)
    )
    private val lvl26 = listOf( // 6 pytań
        Question("10 * 10 = ", listOf("90", "100", "110"), 1),
        Question("2 * 2 * 2 * 2 = ", listOf("14", "16", "18"), 1),
        Question("7 * 4 = ", listOf("26", "28", "30"), 1),
        Question("5 * 2 * 3 = ", listOf("25", "30", "35"), 1),
        Question("12 * 2 = ", listOf("22", "24", "26"), 1),
        Question("3 * 2 * 4 = ", listOf("22", "24", "26"), 1)
    )
    private val lvl27 = listOf( // 3 pytania
        Question("15 * 2 = ", listOf("25", "30", "35"), 1),
        Question("5 * 5 * 2 = ", listOf("40", "50", "60"), 1),
        Question("8 * 4 = ", listOf("30", "32", "34"), 1)
    )
    private val lvl28 = listOf( // 5 pytań
        Question("20 * 3 = ", listOf("50", "60", "70"), 1),
        Question("2 * 2 * 2 * 2 * 2 = ", listOf("30", "32", "34"), 1),
        Question("11 * 4 = ", listOf("42", "44", "46"), 1),
        Question("3 * 3 * 2 = ", listOf("16", "18", "20"), 1),
        Question("9 * 5 = ", listOf("40", "45", "50"), 1)
    )
    private val lvl29 = listOf( // 4 pytania
        Question("25 * 4 = ", listOf("90", "100", "110"), 1),
        Question("4 * 2 * 5 = ", listOf("35", "40", "45"), 1),
        Question("13 * 2 = ", listOf("24", "26", "28"), 1),
        Question("3 * 2 * 3 = ", listOf("16", "18", "20"), 1)
    )
    private val lvl30 = listOf( // 6 pytań
        Question("10 * 6 = ", listOf("50", "60", "70"), 1),
        Question("2 * 2 * 2 * 3 = ", listOf("20", "24", "28"), 1),
        Question("15 * 4 = ", listOf("55", "60", "65"), 1),
        Question("4 * 4 * 2 = ", listOf("30", "32", "34"), 1),
        Question("50 * 2 = ", listOf("90", "100", "110"), 1),
        Question("8 / 2 = ", listOf("3", "4", "5"), 1)
    )

    // DZIELENIE (31-39)
    private val lvl31 = listOf( // 3 pytania
        Question("6 / 2 = ", listOf("2", "3", "4"), 1),
        Question("10 / 2 = ", listOf("4", "5", "6"), 1),
        Question("12 / 3 = ", listOf("3", "4", "5"), 1)
    )
    private val lvl32 = listOf( // 4 pytania
        Question("20 / 4 = ", listOf("4", "5", "6"), 1),
        Question("15 / 5 = ", listOf("2", "3", "4"), 1),
        Question("30 / 6 = ", listOf("4", "5", "6"), 1),
        Question("18 / 2 = ", listOf("8", "9", "10"), 1)
    )
    private val lvl33 = listOf( // 3 pytania
        Question("40 / 5 = ", listOf("7", "8", "9"), 1),
        Question("24 / 3 = ", listOf("7", "8", "9"), 1),
        Question("50 / 10 = ", listOf("4", "5", "6"), 1)
    )
    private val lvl34 = listOf( // 5 pytań
        Question("60 / 6 = ", listOf("9", "10", "11"), 1),
        Question("100 / 2 = ", listOf("45", "50", "55"), 1),
        Question("16 / 4 = ", listOf("3", "4", "5"), 1),
        Question("45 / 5 = ", listOf("8", "9", "10"), 1),
        Question("80 / 8 = ", listOf("9", "10", "11"), 1)
    )
    private val lvl35 = listOf( // 4 pytania
        Question("100 / 10 = ", listOf("9", "10", "11"), 1),
        Question("21 / 3 = ", listOf("6", "7", "8"), 1),
        Question("64 / 8 = ", listOf("7", "8", "9"), 1),
        Question("50 / 5 = ", listOf("9", "10", "11"), 1)
    )
    private val lvl36 = listOf( // 6 pytań
        Question("90 / 9 = ", listOf("9", "10", "11"), 1),
        Question("36 / 4 = ", listOf("8", "9", "10"), 1),
        Question("100 / 4 = ", listOf("20", "25", "30"), 1),
        Question("48 / 6 = ", listOf("7", "8", "9"), 1),
        Question("70 / 7 = ", listOf("9", "10", "11"), 1),
        Question("81 / 9 = ", listOf("8", "9", "10"), 1)
    )
    private val lvl37 = listOf( // 3 pytania
        Question("72 / 8 = ", listOf("8", "9", "10"), 1),
        Question("56 / 7 = ", listOf("7", "8", "9"), 1),
        Question("90 / 3 = ", listOf("25", "30", "35"), 1)
    )
    private val lvl38 = listOf( // 5 pytań
        Question("100 / 5 = ", listOf("15", "20", "25"), 1),
        Question("42 / 6 = ", listOf("6", "7", "8"), 1),
        Question("80 / 4 = ", listOf("15", "20", "25"), 1),
        Question("63 / 9 = ", listOf("6", "7", "8"), 1),
        Question("55 / 5 = ", listOf("10", "11", "12"), 1)
    )
    private val lvl39 = listOf( // 4 pytania
        Question("100 / 1 = ", listOf("90", "100", "110"), 1),
        Question("66 / 6 = ", listOf("10", "11", "12"), 1),
        Question("84 / 2 = ", listOf("40", "42", "44"), 1),
        Question("96 / 3 = ", listOf("30", "32", "34"), 1)
    )

    private val lvl40 = listOf(
        Question("THE END!!", listOf("SEE", "YOU", "SOON"), 2),
    )

    private val levelSequence = listOf(
        lvl1, lvl2, lvl3, lvl4, lvl5, lvl6, lvl7, lvl8, lvl9, lvl10,
        lvl11, lvl12, lvl13, lvl14, lvl15, lvl16, lvl17, lvl18, lvl19, lvl20,
        lvl21, lvl22, lvl23, lvl24, lvl25, lvl26, lvl27, lvl28, lvl29, lvl30,
        lvl31, lvl32, lvl33, lvl34, lvl35, lvl36, lvl37, lvl38, lvl39, lvl40
    )

    private lateinit var buttons: List<Button>
    private lateinit var currLevel: TextView

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

        val settingsBtn = findViewById<ImageButton>(R.id.btnSettings)
        settingsBtn.setOnClickListener {
            showSettingsPopup()
        }

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

    private fun showSettingsPopup() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_settings)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnSound = dialog.findViewById<Button>(R.id.btnSound)
        val btnVibrations = dialog.findViewById<Button>(R.id.btnVibrations)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btnClose)

        fun updateSoundButton() {
            if (VibrationManager.isMusicEnabled()) {
                btnSound.text = "Music: ON"
                btnSound.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3FD054"))
            } else {
                btnSound.text = "Music: OFF"
                btnSound.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BA3030"))
            }
        }

        fun updateVibrationButton() {
            if (VibrationManager.isVibrationEnabled()) {
                btnVibrations.text = "Vibrations: ON"
                btnVibrations.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3FD054"))
            } else {
                btnVibrations.text = "Vibrations: OFF"
                btnVibrations.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BA3030"))
            }
        }

        updateSoundButton()
        updateVibrationButton()

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnSound.setOnClickListener {
            VibrationManager.setMusicEnabled(this, !VibrationManager.isMusicEnabled())
            updateSoundButton()
        }

        btnVibrations.setOnClickListener {
            VibrationManager.setVibrationEnabled(!VibrationManager.isVibrationEnabled())
            updateVibrationButton()
        }

        dialog.show()
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
