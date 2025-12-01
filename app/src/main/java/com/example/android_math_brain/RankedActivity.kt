package com.example.android_math_brain

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random
import android.os.CountDownTimer

class RankedActivity : AppCompatActivity() {

    private val dzialania = listOf('+', '-', '*')

    private var currQuestion = 1;

    private var points = 0;

    private var time = 0;

    private var dzialanie = "dodawanie";

    private var tresc = "2+2"


    private var wynik = -10

    private var correctAnswerIndex = -1

    private var firstNumber = 0
    private var secondNumber = 0
    private var thirdNumber = -1
    private var isThird = false


    private var wrongAnswers = 3


    private lateinit var isGoodText: TextView
    private var countDownTimer: CountDownTimer? = null

    private lateinit var btn1: Button;
    private lateinit var btn2: Button;
    private lateinit var btn3: Button
    var prawidlowaOdp = 0

    private lateinit var progressBar: ProgressBar

    private lateinit var btnReturn: ImageView
    private lateinit var buttons: List<Button>

    private lateinit var stage : TextView




    private val positiveText = listOf<String>(
        "Awesome!",
        "Excellent!",
        "Great job!",
        "Perfect!",
        "You got it!",
        "Super!",
        "That's right!",
        "Unstoppable!",
        "You are the real math brain!"
    )

    private val negativeText = listOf(
        "Try again!",
        "Oops!",
        "Almost there!",
        "Keep trying!",
        "Incorrect.",
        "Better luck next time!",
        "A small miscalculation"
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
        btnReturn.setOnClickListener {goBack()}

        isGoodText = findViewById(R.id.isGoodText)

        progressBar = findViewById(R.id.progressBar)

        generateQuestion()

    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
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
        VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)





        if(selectedAnswerIndex == correctAnswerIndex){
            VibrationManager.vibrate(this, VibrationManager.VibrationType.CORRECT)
            val randomMessage = positiveText.random()
            isGoodText.text = randomMessage
            isGoodText.setTextColor(android.graphics.Color.parseColor("#3FD054")) //ZMIENIAM KOLOR NA POPRAWNY
            excText.setTextColor(android.graphics.Color.parseColor("#3FD054")) //ZMIENIAM KOLOR NA POPRAWNY
            buttons[selectedAnswerIndex].setBackgroundColor(android.graphics.Color.parseColor("#3FD054"))
            buttons.forEach{
                it.isClickable = false
            }
            excText.text = tresc + wynik.toString();

            Handler(Looper.getMainLooper()).postDelayed({
                excText.setTextColor(android.graphics.Color.parseColor("#1a3d59")) //ZMIENIAM KOLOR NA ZWYKLY
                buttons.forEach{
                    it.isClickable = true
                }
                buttons.forEach{
                    it.setBackgroundColor(android.graphics.Color.parseColor("#99c1f1")) //zmiana koloru na zwykly
                }
                isGoodText.text = ""


                currQuestion++

                stage.text = "Stage: " + currQuestion.toString()



                generateQuestion()
            }, 1000)

        }


        else{
            wrongAnswers--
            VibrationManager.vibrate(this, VibrationManager.VibrationType.WRONG)

            showToast(this, "YOU HAVE " + (wrongAnswers).toString() + " WRONG ANSWERS LEFT")



            isGoodText.setTextColor(android.graphics.Color.parseColor("#BA3030")) //ZMIENIAM KOLOR NA NIEPOPRAWNY
            excText.setTextColor(android.graphics.Color.parseColor("#BA3030")) //ZMIENIAM KOLOR NA NIEOPRAWNY
            currQuestion--
            stage.text = "Stage: " + currQuestion.toString()

            buttons[prawidlowaOdp].setBackgroundColor(android.graphics.Color.parseColor("#3FD054")) //poprawny btn
            buttons[selectedAnswerIndex].setBackgroundColor(android.graphics.Color.parseColor("#BA3030"))
            buttons.forEach{
                it.isClickable = false
            }
            excText.text = tresc + wynik.toString();
            isGoodText.text = negativeText.random()


            Handler(Looper.getMainLooper()).postDelayed({
                if(wrongAnswers <=0){
                    handleTimeout(isGameOver = true)
                }
                else {
                    buttons.forEach {
                        it.setBackgroundColor(android.graphics.Color.parseColor("#99c1f1"))
                    }
                    buttons.forEach {
                        it.isClickable = true
                    }
                    excText.setTextColor(android.graphics.Color.parseColor("#1a3d59"))
                    isGoodText.text = ""
                    generateQuestion()

                }
            }, 1500)

        }




    }
    private fun generateQuestion(){
        if(currQuestion<4) {
            firstNumber = (1..10).random()
            secondNumber = (1..10).random()
        }
        else{
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
        btn1.text =
            if(prawidlowaOdp == 0){
                wynik.toString()
            }
            else{
                val pom = (1..2).random()
                val pom2 = (1..7).random()
                if(pom == 1){
                    (wynik-pom2).toString()
                }
                else{
                    (wynik+pom2).toString()
                }
            }

        btn2.text =
            if(prawidlowaOdp == 1){
                wynik.toString()
            }
            else{
                val pom = (1..2).random()
                val pom2 = (1..7).random()

                if(pom == 1){
                    (wynik-pom2).toString()
                }
                else{
                    (wynik+pom2).toString()
                }
            }

        btn3.text =
            if(prawidlowaOdp == 2){
                wynik.toString()
            }
            else{
                val pom = (1..2).random()
                val pom2 = (1..7).random()

                if(pom == 1){
                    (wynik-pom2).toString()
                }
                else{
                    (wynik+pom2).toString()
                }
            }






    }


    private fun generowanieTresci(){
        dzialanie = dzialania.random().toString()
        tresc = when(dzialanie){
            "+" -> {
                if(!isThird) {
                    wynik = firstNumber + secondNumber
                    "$firstNumber + $secondNumber = "
                }
                else{
                    val pom = (1 .. 3).random()
                    if(pom == 1) {
                        wynik = firstNumber + secondNumber + thirdNumber
                        "$firstNumber + $secondNumber + $thirdNumber = "

                    }
                    else if (pom == 2){
                        wynik = firstNumber + secondNumber - thirdNumber
                        "$firstNumber + $secondNumber - $thirdNumber = "

                    }
                    else{
                        wynik = firstNumber * secondNumber - thirdNumber
                        "$firstNumber * $secondNumber - $thirdNumber = "
                    }
                }
            }

            "-" -> {
                if(!isThird) {
                    wynik = firstNumber - secondNumber
                    "$firstNumber - $secondNumber = "
                }
                else{
                    val pom = (1 .. 3).random()
                    if(pom == 1){
                        wynik = firstNumber - secondNumber + thirdNumber
                        "$firstNumber - $secondNumber + $thirdNumber = "

                    }
                    else if (pom == 2){
                        wynik = firstNumber - secondNumber - thirdNumber
                        "$firstNumber - $secondNumber - $thirdNumber ="

                    }
                    else{
                        wynik = firstNumber * secondNumber - thirdNumber
                        "$firstNumber * $secondNumber - $thirdNumber = "
                    }

                }
            }
            "*" -> {
                if(!isThird){wynik = firstNumber * secondNumber
                    "$firstNumber * $secondNumber = "}
                else{
                    val pom = (1 .. 4).random()
                    if(pom == 1){
                        wynik = firstNumber * secondNumber * thirdNumber
                        "$firstNumber * $secondNumber * $thirdNumber = "
                    }
                    else if (pom == 2){
                        val parzystaLiczba = (1..50).random() * 2
                        wynik = parzystaLiczba / 2
                        "$parzystaLiczba : 2 = "
                    }
                    else if(pom == 3){
                        val podzielnaPrzezTrzy = (1..33).random() * 3
                        wynik = podzielnaPrzezTrzy / 3
                        "$podzielnaPrzezTrzy : 3 = "
                    }
                    else{
                        val podzielnaPrzezPiec = (1..20).random() * 5
                        wynik = podzielnaPrzezPiec / 5
                        "$podzielnaPrzezPiec : 5 = "
                    }
                }

            }
            else -> "hdfabhsafdblksfd"
        }



        excText.text = tresc

    }


    private fun handleTimeout(isGameOver: Boolean = false) {
        VibrationManager.vibrate(this, VibrationManager.VibrationType.WRONG)

        isGoodText.setTextColor(android.graphics.Color.parseColor("#BA3030"))
        excText.setTextColor(android.graphics.Color.parseColor("#BA3030"))


        buttons[correctAnswerIndex].setBackgroundColor(android.graphics.Color.parseColor("#3FD054"))
        buttons.forEach{
            it.isClickable = false
        }

        excText.text = tresc + wynik.toString()





        //TUTAJ MASZ PUNKTACJE

        points = when {
            currQuestion<4 -> -10
            currQuestion<8 -> -7
            currQuestion<10 -> 0
            currQuestion<15 -> 5
            currQuestion<20 -> 10
            currQuestion<25 -> 20
            else -> 40
        }

        // PUNKTACJA




        GameData.getInstance(this).addPoints(points)
        if (isGameOver || wrongAnswers <= 0) {
            isGoodText.text = "You lost and got: $points points"
        } else {
            isGoodText.text = "Time's Up! You got: $points points"
        }


        Handler(Looper.getMainLooper()).postDelayed({

            buttons.forEach{
                it.setBackgroundColor(android.graphics.Color.parseColor("#99c1f1"))
                it.isClickable = true
            }
            excText.setTextColor(android.graphics.Color.parseColor("#1a3d59"))
            isGoodText.text = ""

            Intent(this, MainActivity::class.java).also {


                intent.putExtra("points", points)
                startActivity(it)
                finish()


            }



        }, 5000)

    }

    private fun lolixFun(){

        if(currQuestion <=5){
            time = 7;
            generowanieTresci()

        }
        else if(currQuestion<=15){
            time = 6;
            generowanieTresci()
        }
        else if(currQuestion <=25){
            time = 5
            generowanieTresci()
        }
        else if(currQuestion <=35) {
            time = 4
            generowanieTresci()
        }
        else if(currQuestion <=45) {
            time = 3
            generowanieTresci()
        }

        else{
            time = 2
            generowanieTresci()
        }


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

