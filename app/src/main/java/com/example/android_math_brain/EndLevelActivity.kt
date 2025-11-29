package com.example.android_math_brain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.net.Uri
import android.widget.VideoView


class EndLevelActivity : AppCompatActivity() {

    private var allAnswers = -1;
    private var passedLevel = -1;
    private var wrongAnswers = -1;

    private var procenty = -1;
    private var isWon = false;

    private lateinit var brainImg: ImageView
    private lateinit var textUnderBrain: TextView
    private lateinit var stars1: ImageView
    private lateinit var scoreText: TextView
    private lateinit var homeBtn: Button
    private lateinit var tryAgainBtn: Button
    private lateinit var nextLevelBtn: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        val gameData = GameData.getInstance(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_end_level)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        biore z poprzedniego widoku to co potrzebne
        passedLevel = intent.getIntExtra("passed_level", -1)
        allAnswers = intent.getIntExtra("all_answers", -1)
        wrongAnswers = intent.getIntExtra("wrong_answers", -1)
        procenty = intent.getIntExtra("procenty", -1)
        isWon = intent.getBooleanExtra("isWon", false)

// ==================================================================================

//        jakby jakis blad byl to wywala
        if(passedLevel == -1 || allAnswers == -1 || wrongAnswers == -1 || procenty == -1){
            finish()
            return
        }
//        ===============================================================

        val aktualnyPoziom = gameData.getLevel()








//to co powyzej nie musisz ruszac
        //ZMIENNE KTORE CI SIE PRZYDADZA
        // 1. procenty <- ma w sobie inta z procentami np 33 juz wszystko policzone wystarczy napisac scoreText.text
        // 2. isWon <-> to boolean ma w sobie true albo false, jesli true to znaczy ze level jest zaliczony a false to niezaliczony
        // 3. wrongAnswers - liczba wszystkich zlych odpowiedzi w int
        // 4. allAnswers - liczba wszystkich odpowiedzi  czyli ile pytan bylo w int
        // 5. passedLevel - poziom ktory gracz zaliczyl w int
        // 6. wszystkie zmienne z findviewid ktore masz pod spodem to raczej ogarniasz nazwalem je tak samo jakie maja id w activity_end_level
        // 7. aktualnyPoziom pokazuje aktualny poziom z pamieci




//        brainImg = findViewById(R.id.brainImg)
        textUnderBrain = findViewById(R.id.textUnderBrain)
        stars1 = findViewById(R.id.star1)
        scoreText = findViewById(R.id.scoreText)
        homeBtn = findViewById(R.id.homeBtn)
        tryAgainBtn = findViewById(R.id.tryAgainBtn)
        nextLevelBtn = findViewById(R.id.nextLevelBtn)
        val brainWin = findViewById<ImageView>(R.id.brainWin)


        if(procenty<0){
            procenty = 0
        }
        //ten if zeby na minus nie wypioerdalalo

        if (isWon && procenty in 32..55) {
            textUnderBrain.text = "LEVEL CLEARD!"
            brainWin.setImageResource(R.drawable.brainguybook)
            stars1.setImageResource(R.drawable.star1)
            scoreText.text = "Score: $procenty%"
        } else if(isWon && procenty in 51..75) {
            textUnderBrain.text = "LEVEL CLEARD!"
            stars1.setImageResource(R.drawable.star2)
            brainWin.setImageResource(R.drawable.braindumbels)
            scoreText.text = "Score: $procenty%"
        } else if (isWon && procenty in 76..100) {
            textUnderBrain.text = "LEVEL CLEARD!"
            stars1.setImageResource(R.drawable.stars3)
            brainWin.setImageResource(R.drawable.happybrain2)
            scoreText.text = "Score: $procenty%"
        }else {
            textUnderBrain.text = "LEVEL FAILED!"
            stars1.setImageResource(R.drawable.star0)
            brainWin.setImageResource(R.drawable.sadbrain)
            scoreText.text = "Score: $procenty%"
        }


//        dzialanie video
//        val videoResId = when {
//            isWon && procenty in 32..55 -> R.raw.happybrain
//            isWon && procenty in 51..75 -> R.drawable.brainguybook
//            isWon && procenty in 76..100 -> R.drawable.happybrain2
//            else -> R.raw.walkbrain
//        }
//
//        val videoUri = Uri.parse("android.resource://${packageName}/$videoResId")
//        brainVideo.setVideoURI(videoUri)
//        brainVideo.setOnPreparedListener { mp ->
//            mp.isLooping = true
//            brainVideo.start()
//        }

//koniec dzialania video







        //onclicki zwykle chcesz to pozmieniaj, to jak sie wywoluje funkcje masz w 126linijce i co robia
        homeBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            val intent = Intent(this, AdventureActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
        }
        nextLevelBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            val intent = Intent(this, LevelActivity::class.java)
            intent.putExtra("LEVEL_ID", passedLevel+1)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        tryAgainBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            val intent = Intent(this, LevelActivity::class.java)
            intent.putExtra("LEVEL_ID", passedLevel)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }








    }


    //Jak wywolasz funkcje backHome to wracasz do AdventureActivity wywolujesz backHome()
    //Jak wywolasz funkcje tryAgain to wracasz do tego levela w ktory grales wywolujesz tryAgain()
    //Jak wywolasz funkcje nextLvl to idziesz do next lvla wywolujesz nextLvl()


    //najlepiej nic nie zmieniaj pod spodem bo dziala chyba git
    private fun backHome(){
        Intent(this, AdventureActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun tryAgain(){
        Intent(this, LevelActivity::class.java).also {
            it.putExtra("LEVEL_ID", passedLevel)
            startActivity(it)
            finish()
        }
    }

    private fun nextLvl(){
        if(GameData.getInstance(this).getLevel() == passedLevel){
            showToast(this, "You didnt completed " + GameData.getInstance(this).getLevel() + " level")
            backHome()
            return
        }
        Intent(this, LevelActivity::class.java).also {
            it.putExtra("LEVEL_ID", passedLevel+1)
            startActivity(it)
            finish()
        }
    }
}