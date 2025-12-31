package com.example.android_math_brain

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random
import android.os.CountDownTimer
import android.media.SoundPool

class RankedActivity : AppCompatActivity() {

    private val dzialania = listOf('+', '-', '*')
    private var currQuestion = 1
    private var points = 0
    private var time = 0
    private var dzialanie = "dodawanie"
    private var tresc = "2+2"
    private var wynik = -10
    private var correctAnswerIndex = -1
    private var firstNumber = 0
    private var secondNumber = 0
    private var thirdNumber = -1
    private var isThird = false
    private var wrongAnswers = 3

    private lateinit var soundPool: SoundPool
    private var badSoundId: Int = 0
    private var goodSoundId: Int = 0
    private var soundReady = false

    private lateinit var isGoodText: TextView
    private var countDownTimer: CountDownTimer? = null
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    var prawidlowaOdp = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var btnReturn: ImageView
    private lateinit var buttons: List<Button>
    private lateinit var stage : TextView
    private lateinit var hearts: List<ImageView>

    private val positiveText = listOf<String>(
        "Awesome!", "Excellent!", "Great job!", "Perfect!", "You got it!",
        "Super!", "That's right!", "Unstoppable!", "You are the real math brain!"
    )

    private val negativeText = listOf(
        "Try again!", "Oops!", "Almost there!", "Keep trying!",
        "Incorrect.", "Better luck next time!", "A small miscalculation"
    )

    private lateinit var excText: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ranked)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val settingsBtn = findViewById<ImageButton>(R.id.btnSettings)
        settingsBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            showSettingsPopup()
        }

        hearts = listOf(
            findViewById(R.id.heart1),
            findViewById(R.id.heart2),
            findViewById(R.id.heart3)
        )

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

        btn1 = findViewById<Button>(R.id.answer1ranked)
        btn2 = findViewById<Button>(R.id.answer2ranked)
        btn3 = findViewById<Button>(R.id.answer3ranked)
        excText = findViewById(R.id.excTextRanked)

        btn1.setOnClickListener { checkAnswer(0) }
        btn2.setOnClickListener { checkAnswer(1) }
        btn3.setOnClickListener { checkAnswer(2) }

        buttons = listOf(btn1, btn2, btn3)
        stage = findViewById(R.id.currentStage)
        stage.text = "Stage: " + currQuestion.toString()

        btnReturn = findViewById(R.id.btnReturnRank)
        btnReturn.setOnClickListener { goBack() }

        isGoodText = findViewById(R.id.isGoodText)
        progressBar = findViewById(R.id.progressBar)

        updateHearts()
        generateQuestion()
    }

    private fun updateHearts() {
        for (i in hearts.indices) {
            hearts[i].visibility = if (i < wrongAnswers) View.VISIBLE else View.GONE
        }
    }

    private fun showSettingsPopup() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_settings)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.findViewById<Button>(R.id.btnResetRanking).visibility = View.GONE

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
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            dialog.dismiss()
        }

        btnSound.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            VibrationManager.setMusicEnabled(this, !VibrationManager.isMusicEnabled())
            updateSoundButton()
        }

        btnVibrations.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            VibrationManager.setVibrationEnabled(!VibrationManager.isVibrationEnabled())
            updateVibrationButton()
        }

        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        soundPool.release()
    }

    private fun goBack(){
        countDownTimer?.cancel()
        VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
        }
    }

    private fun checkAnswer(selectedAnswerIndex: Int){
        countDownTimer?.cancel()

        if(selectedAnswerIndex == correctAnswerIndex){
            if (soundReady) soundPool.play(goodSoundId, 1f, 1f, 1, 0, 1f)

            VibrationManager.vibrate(this, VibrationManager.VibrationType.CORRECT)
            val randomMessage = positiveText.random()
            isGoodText.text = randomMessage
            isGoodText.setTextColor(Color.parseColor("#3FD054"))
            excText.setTextColor(Color.parseColor("#3FD054"))
            buttons[selectedAnswerIndex].setBackgroundColor(Color.parseColor("#3FD054"))
            buttons.forEach{
                it.isClickable = false
            }
            excText.text = tresc + wynik.toString();

            Handler(Looper.getMainLooper()).postDelayed({
                excText.setTextColor(Color.parseColor("#1a3d59"))
                buttons.forEach{
                    it.isClickable = true
                    it.setBackgroundColor(Color.parseColor("#99c1f1"))
                }
                isGoodText.text = ""
                currQuestion++
                stage.text = "Stage: " + currQuestion.toString()
                generateQuestion()
            }, 1000)
        } else {
            // Wrong answer logic
            if (soundReady) soundPool.play(badSoundId, 1f, 1f, 1, 0, 1f)

            wrongAnswers--
            updateHearts()
            VibrationManager.vibrate(this, VibrationManager.VibrationType.WRONG)

            isGoodText.setTextColor(Color.parseColor("#BA3030"))
            excText.setTextColor(Color.parseColor("#BA3030"))

            stage.text = "Stage: " + currQuestion.toString()

            buttons[prawidlowaOdp].setBackgroundColor(Color.parseColor("#3FD054"))
            buttons[selectedAnswerIndex].setBackgroundColor(Color.parseColor("#BA3030"))
            buttons.forEach{
                it.isClickable = false
            }
            excText.text = tresc + wynik.toString();
            isGoodText.text = negativeText.random()

            Handler(Looper.getMainLooper()).postDelayed({
                if(wrongAnswers <=0){
                    gameOver("You lost!")
                } else {
                    // Reset for next question
                    buttons.forEach {
                        it.setBackgroundColor(Color.parseColor("#99c1f1"))
                        it.isClickable = true
                    }
                    excText.setTextColor(Color.parseColor("#1a3d59"))
                    isGoodText.text = ""
                    generateQuestion()
                }
            }, 1500)
        }
    }

    private fun gameOver(message: String) {
        points = when {
            currQuestion < 4 -> -10
            currQuestion < 8 -> -7
            currQuestion < 10 -> 0
            currQuestion < 15 -> 5
            currQuestion < 20 -> 10
            currQuestion < 25 -> 20
            else -> 40
        }
        GameData.getInstance(this).addPoints(points)
        isGoodText.text = "$message You got: $points points"

        buttons.forEach{
            it.isClickable = false
        }

        Handler(Looper.getMainLooper()).postDelayed({
            Intent(this, MainActivity::class.java).also {
                it.putExtra("points", points)
                startActivity(it)
                finish()
            }
        }, 3000)
    }

    private fun handleTimeout() {
        if (soundReady) soundPool.play(badSoundId, 1f, 1f, 1, 0, 1f)
        VibrationManager.vibrate(this, VibrationManager.VibrationType.WRONG)

        wrongAnswers--
        updateHearts()

        isGoodText.text = "Time's up!"
        isGoodText.setTextColor(Color.parseColor("#BA3030"))
        excText.setTextColor(Color.parseColor("#BA3030"))

        stage.text = "Stage: " + currQuestion.toString()

        buttons[correctAnswerIndex].setBackgroundColor(Color.parseColor("#3FD054"))
        buttons.forEach { it.isClickable = false }
        excText.text = tresc + wynik.toString()

        Handler(Looper.getMainLooper()).postDelayed({
            if (wrongAnswers <= 0) {
                gameOver("Time's up!")
            } else {
                buttons.forEach {
                    it.setBackgroundColor(Color.parseColor("#99c1f1"))
                    it.isClickable = true
                }
                excText.setTextColor(Color.parseColor("#1a3d59"))
                isGoodText.text = ""
                generateQuestion()
            }
        }, 1500)
    }

    private fun generateQuestion(){
        if(currQuestion<4) {
            firstNumber = (1..10).random()
            secondNumber = (1..10).random()
        } else {
            firstNumber = (1..10).random()
            secondNumber = (1..10).random()
            isThird = Random.nextBoolean()
            if(isThird){
                thirdNumber = (1..10).random()
            }
        }
        lolixFun()

        prawidlowaOdp = (0..2).random()
        correctAnswerIndex = prawidlowaOdp
        btn1.text = if(prawidlowaOdp == 0) wynik.toString() else {
            val pom = (1..2).random(); val pom2 = (1..7).random()
            if(pom == 1) (wynik-pom2).toString() else (wynik+pom2).toString()
        }
        btn2.text = if(prawidlowaOdp == 1) wynik.toString() else {
            val pom = (1..2).random(); val pom2 = (1..7).random()
            if(pom == 1) (wynik-pom2).toString() else (wynik+pom2).toString()
        }
        btn3.text = if(prawidlowaOdp == 2) wynik.toString() else {
            val pom = (1..2).random(); val pom2 = (1..7).random()
            if(pom == 1) (wynik-pom2).toString() else (wynik+pom2).toString()
        }
    }

    private fun generowanieTresci(){
        dzialanie = dzialania.random().toString()
        tresc = when(dzialanie){
            "+" -> {
                if(!isThird) {
                    wynik = firstNumber + secondNumber
                    "$firstNumber + $secondNumber = "
                } else {
                    val pom = (1 .. 3).random()
                    if(pom == 1) {
                        wynik = firstNumber + secondNumber + thirdNumber
                        "$firstNumber + $secondNumber + $thirdNumber = "
                    } else if (pom == 2){
                        wynik = firstNumber + secondNumber - thirdNumber
                        "$firstNumber + $secondNumber - $thirdNumber = "
                    } else {
                        wynik = firstNumber * secondNumber - thirdNumber
                        "$firstNumber * $secondNumber - $thirdNumber = "
                    }
                }
            }
            "-" -> {
                if(!isThird) {
                    wynik = firstNumber - secondNumber
                    "$firstNumber - $secondNumber = "
                } else {
                    val pom = (1 .. 3).random()
                    if(pom == 1){
                        wynik = firstNumber - secondNumber + thirdNumber
                        "$firstNumber - $secondNumber + $thirdNumber = "
                    } else if (pom == 2){
                        wynik = firstNumber - secondNumber - thirdNumber
                        "$firstNumber - $secondNumber - $thirdNumber ="
                    } else {
                        wynik = firstNumber * secondNumber - thirdNumber
                        "$firstNumber * $secondNumber - $thirdNumber = "
                    }
                }
            }
            "*" -> {
                if(!isThird){
                    wynik = firstNumber * secondNumber
                    "$firstNumber * $secondNumber = "
                } else {
                    val pom = (1 .. 4).random()
                    if(pom == 1){
                        wynik = firstNumber * secondNumber * thirdNumber
                        "$firstNumber * $secondNumber * $thirdNumber = "
                    } else if (pom == 2){
                        val parzystaLiczba = (1..50).random() * 2
                        wynik = parzystaLiczba / 2
                        "$parzystaLiczba : 2 = "
                    } else if(pom == 3){
                        val podzielnaPrzezTrzy = (1..33).random() * 3
                        wynik = podzielnaPrzezTrzy / 3
                        "$podzielnaPrzezTrzy : 3 = "
                    } else {
                        val podzielnaPrzezPiec = (1..20).random() * 5
                        wynik = podzielnaPrzezPiec / 5
                        "$podzielnaPrzezPiec : 5 = "
                    }
                }
            }
            else -> ""
        }
        excText.text = tresc
    }

    private fun lolixFun(){
        if(currQuestion <=5) time = 7
        else if(currQuestion<=15) time = 6
        else if(currQuestion <=25) time = 5
        else if(currQuestion <=35) time = 4
        else if(currQuestion <=45) time = 3
        else time = 2

        generowanieTresci()
        startTimer(time)
    }

    private fun startTimer(seconds: Int) {
        countDownTimer?.cancel()
        progressBar.max = 100
        progressBar.progress = 100
        val totalTimeMillis = seconds * 1000L
        val intervalMillis = 10L

        countDownTimer = object : CountDownTimer(totalTimeMillis, intervalMillis) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished * 100 / totalTimeMillis).toInt()
                progressBar.progress = progress
            }
            override fun onFinish() {
                progressBar.progress = 0
                handleTimeout()
            }
        }.start()
    }
}
