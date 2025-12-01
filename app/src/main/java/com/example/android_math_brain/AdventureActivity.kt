package com.example.android_math_brain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdventureActivity : AppCompatActivity() {

    private fun handleLevelClick(v: View){
        val currLvl = gameData.getLevel()

        val clickedLevel = when(v.id) {
            R.id.button1 -> 1
            R.id.button2 -> 2
            R.id.button3 -> 3
            R.id.button4 -> 4
            R.id.button5 -> 5
            R.id.button6 -> 6
            R.id.button7 -> 7
            R.id.button8 -> 8
            R.id.button9 -> 9
            R.id.button10 -> 10
            R.id.button11 -> 11
            R.id.button12 -> 12
            R.id.button13 -> 13
            R.id.button14 -> 14
            R.id.button15 -> 15
            R.id.button16 -> 16
            R.id.button17 -> 17
            R.id.button18 -> 18
            R.id.button19 -> 19
            R.id.button20 -> 20
            R.id.button21 -> 21
            R.id.button22 -> 22
            R.id.button23 -> 23
            R.id.button24 -> 24
            R.id.button25 -> 25
            R.id.button26 -> 26
            R.id.button27 -> 27
            R.id.button28 -> 28
            R.id.button29 -> 29
            R.id.button30 -> 30
            R.id.button31 -> 31
            R.id.button32 -> 32
            R.id.button33 -> 33
            R.id.button34 -> 34
            R.id.button35 -> 35
            R.id.button36 -> 36
            R.id.button37 -> 37
            R.id.button38 -> 38
            R.id.button39 -> 39
            R.id.button40 -> 40
            else -> 0
        }

        if (clickedLevel == 0) return

        when {
            clickedLevel > currLvl -> {
                VibrationManager.vibrate(this, VibrationManager.VibrationType.LOCKED)
                showToast(this, "Your current level is $currLvl, try to pass it!")
            }
            clickedLevel == currLvl -> {
                VibrationManager.vibrate(this, VibrationManager.VibrationType.CURRENT)
                val intent = Intent(this, LevelActivity::class.java)
                intent.putExtra("LEVEL_ID", clickedLevel)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
            }
            clickedLevel < currLvl -> {
                VibrationManager.vibrate(this, VibrationManager.VibrationType.PASSED)

                val intent = Intent(this, LevelActivity::class.java)
                intent.putExtra("LEVEL_ID", clickedLevel)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
            }
        }




    }

    private lateinit var gameData: GameData // nie usuwaj

    override fun onCreate(savedInstanceState: Bundle?) {
        gameData = GameData.getInstance(this) // nie usuwaj

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adventure)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val scrollView = findViewById<ScrollView>(R.id.scrollableContent)
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

        // wszystkie guziki w listofie zeby latwiej bylo wywolywac
        val buttons = listOf<Button>(
            findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3),
            findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6),
            findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9),
            findViewById(R.id.button10), findViewById(R.id.button11), findViewById(R.id.button12),
            findViewById(R.id.button13), findViewById(R.id.button14), findViewById(R.id.button15),
            findViewById(R.id.button16), findViewById(R.id.button17), findViewById(R.id.button18),
            findViewById(R.id.button19), findViewById(R.id.button20), findViewById(R.id.button21),
            findViewById(R.id.button22), findViewById(R.id.button23), findViewById(R.id.button24),
            findViewById(R.id.button25), findViewById(R.id.button26), findViewById(R.id.button27),
            findViewById(R.id.button28), findViewById(R.id.button29), findViewById(R.id.button30),
            findViewById(R.id.button31), findViewById(R.id.button32), findViewById(R.id.button33),
            findViewById(R.id.button34), findViewById(R.id.button35), findViewById(R.id.button36),
            findViewById(R.id.button37), findViewById(R.id.button38), findViewById(R.id.button39),
            findViewById(R.id.button40)
        )

        // wywolywanie tego lsitofa zajebistego
        buttons.forEach { button ->
            button.setOnClickListener(::handleLevelClick)
        }

        //kolory gdy lvl jest odblokowany lub zablkoowany

        val currLvl = gameData.getLevel()

        buttons.forEachIndexed { index, button ->
            val levelNumber = index + 1
            button.setOnClickListener(::handleLevelClick)

            if (levelNumber > currLvl) {
                when (levelNumber) {
                    in 1..10 -> {
                        button.setBackgroundResource(R.drawable.button_shadow_add_blocked)
                        button.setTextColor(Color.parseColor("#5F8968"))
                    }
                    in 11..20 -> {
                        button.setBackgroundResource(R.drawable.button_shadow_sub_blocked)
                        button.setTextColor(Color.parseColor("#6b586b"))
                    }
                    in 21..30 -> {
                        button.setBackgroundResource(R.drawable.button_shadow_mul_blocked)
                        button.setTextColor(Color.parseColor("#8B5153"))
                    }
                    in 31..40 -> {
                        button.setBackgroundResource(R.drawable.button_shadow_div_blocked)
                        button.setTextColor(Color.parseColor("#8C8C63"))
                    }
                }
            } else {
                // ODBLOKOWANY LEVEL - analogicznie jak w when
                when (levelNumber) {
                    in 1..10 -> {
                        button.setBackgroundResource(R.drawable.button_shadow_add)
                        button.setTextColor(Color.parseColor("#22B045"))
                    }
                    in 11..20 -> {
                        button.setBackgroundResource(R.drawable.button_shadow_sub)
                        button.setTextColor(Color.parseColor("#793679"))
                    }
                    in 21..30 -> {
                        button.setBackgroundResource(R.drawable.button_shadow_mul)
                        button.setTextColor(Color.parseColor("#A30D14"))
                    }
                    in 31..40 -> {
                        button.setBackgroundResource(R.drawable.button_shadow_div)
                        button.setTextColor(Color.parseColor("#C5BB00"))
                    }
                }
                button.isEnabled = true
            }
        }




        val chapterName = findViewById<TextView>(R.id.chapterName)
        val chapter = findViewById<TextView>(R.id.chapter)
        val topLayout = findViewById<LinearLayout>(R.id.topLayout)
        val goBack = findViewById<ImageButton>(R.id.goBack)

        goBack.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
            finish()
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


        val button11 = findViewById<Button>(R.id.button11)
        val button15 = findViewById<Button>(R.id.button15)
        val button21 = findViewById<Button>(R.id.button21)
        val button25 = findViewById<Button>(R.id.button25)
        val button31 = findViewById<Button>(R.id.button31)
        val button35 = findViewById<Button>(R.id.button35)

        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollBounds = Rect()
            scrollView.getHitRect(scrollBounds)

            if (button11.getLocalVisibleRect(scrollBounds) || button15.getLocalVisibleRect(scrollBounds)) {
                chapterName.text = "SUBTRACTION"
                chapterName.setTextColor(Color.parseColor("#793679"))
                chapter.setTextColor(Color.parseColor("#793679"))
                topLayout.setBackgroundResource(R.drawable.rounded_layout_sub)
                goBack.setImageResource(R.drawable.leftarrowsub)
                goBack.background.setColorFilter(Color.parseColor("#A249A3"), PorterDuff.Mode.SRC_IN)

            } else if (button21.getLocalVisibleRect(scrollBounds) || button25.getLocalVisibleRect(scrollBounds)) {
                chapterName.text = "MULTIPLICATION"
                chapterName.setTextColor(Color.parseColor("#A30D14"))
                chapter.setTextColor(Color.parseColor("#A30D14"))
                topLayout.setBackgroundResource(R.drawable.rounded_layout_mul)
                goBack.setImageResource(R.drawable.leftarrowmul)
                goBack.background.setColorFilter(Color.parseColor("#EB1C24"), PorterDuff.Mode.SRC_IN)

            } else if (button31.getLocalVisibleRect(scrollBounds) || button35.getLocalVisibleRect(scrollBounds)) {
                chapterName.text = "DIVISION"
                chapterName.setTextColor(Color.parseColor("#C5BB00"))
                chapter.setTextColor(Color.parseColor("#C5BB00"))
                topLayout.setBackgroundResource(R.drawable.rounded_layout_div)
                goBack.setImageResource(R.drawable.leftarrowdiv)
                goBack.background.setColorFilter(Color.parseColor("#FDF000"), PorterDuff.Mode.SRC_IN)

            } else {
                chapterName.text = "ADDITION"
                chapterName.setTextColor(Color.parseColor("#22B045"))
                chapter.setTextColor(Color.parseColor("#22B045"))
                topLayout.setBackgroundResource(R.drawable.rounded_layout)
                goBack.setImageResource(R.drawable.leftarrowadd)
                goBack.background.setColorFilter(Color.parseColor("#B4E41D"), PorterDuff.Mode.SRC_IN
                )
            }
        }
    }
}
