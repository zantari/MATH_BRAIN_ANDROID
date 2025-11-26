package com.example.android_math_brain

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
        var currLvl = gameData.getLevel()
        var clickedLevel = 0

        when(v.id){
            R.id.button1 -> clickedLevel = 1
            R.id.button2 -> clickedLevel = 2
            R.id.button3 -> clickedLevel = 3
            R.id.button4 -> clickedLevel = 4
            //itd
        }

        if(clickedLevel >0 && clickedLevel == currLvl){
            val intent = Intent(this, LevelActivity::class.java)
            intent.putExtra("LEVEL_ID", clickedLevel)
            startActivity(intent)

            return
        }
        else if(clickedLevel > currLvl) {
            Toast.makeText(this, "Your current level is "+currLvl+", try to pass it!", Toast.LENGTH_SHORT).show()
            return
        }
        else if(clickedLevel < currLvl) {
            Toast.makeText(this, "You have already passed this level", Toast.LENGTH_SHORT).show()
            return
        }

        return
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

        val buttonPowrot = findViewById<ImageButton>(R.id.goBack)
        buttonPowrot.setOnClickListener {
            finish()
        }
        
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)
        val button10 = findViewById<Button>(R.id.button10)
        val button11 = findViewById<Button>(R.id.button11)
        val button12 = findViewById<Button>(R.id.button12)
        val button13 = findViewById<Button>(R.id.button13)
        val button14 = findViewById<Button>(R.id.button14)
        val button15 = findViewById<Button>(R.id.button15)
        val button16 = findViewById<Button>(R.id.button16)
        val button17 = findViewById<Button>(R.id.button17)
        val button18 = findViewById<Button>(R.id.button18)
        val button19 = findViewById<Button>(R.id.button19)
        val button20 = findViewById<Button>(R.id.button20)
        val button21 = findViewById<Button>(R.id.button21)
        val button22 = findViewById<Button>(R.id.button22)
        val button23 = findViewById<Button>(R.id.button23)
        val button24 = findViewById<Button>(R.id.button24)
        val button25 = findViewById<Button>(R.id.button25)
        val button26 = findViewById<Button>(R.id.button26)
        val button27 = findViewById<Button>(R.id.button27)
        val button28 = findViewById<Button>(R.id.button28)
        val button29 = findViewById<Button>(R.id.button29)
        val button30 = findViewById<Button>(R.id.button30)
        val button31 = findViewById<Button>(R.id.button31)
        val button32 = findViewById<Button>(R.id.button32)
        val button33 = findViewById<Button>(R.id.button33)
        val button34 = findViewById<Button>(R.id.button34)
        val button35 = findViewById<Button>(R.id.button35)
        val button36 = findViewById<Button>(R.id.button36)
        val button37 = findViewById<Button>(R.id.button37)
        val button38 = findViewById<Button>(R.id.button38)
        val button39 = findViewById<Button>(R.id.button39)
        val button40 = findViewById<Button>(R.id.button40)


        val chapterName = findViewById<TextView>(R.id.chapterName)
        val topLayout = findViewById<LinearLayout>(R.id.topLayout)
        val goBack = findViewById<ImageButton>(R.id.goBack)






        button1.setOnClickListener(::handleLevelClick)
        button2.setOnClickListener(::handleLevelClick)
        button3.setOnClickListener(::handleLevelClick)
        button4.setOnClickListener(::handleLevelClick)
        button5.setOnClickListener(::handleLevelClick)







        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollBounds = Rect()
            scrollView.getHitRect(scrollBounds)

            // Sprawdzenie, czy przycisk 11 jest widoczny
            if (button11.getLocalVisibleRect(scrollBounds) || button14.getLocalVisibleRect(scrollBounds)) {
                chapterName.text = "SUBTRACTION"
                chapterName.setTextColor(Color.parseColor("#793679"))
                topLayout.setBackgroundResource(R.drawable.rounded_layout_sub)
                goBack.background.setColorFilter(Color.parseColor("#A249A3"), PorterDuff.Mode.SRC_IN)

            } else if (button21.getLocalVisibleRect(scrollBounds) || button24.getLocalVisibleRect(scrollBounds)) {
                chapterName.text = "MULTIPLICATION"
                chapterName.setTextColor(Color.parseColor("#A30D14"))
                topLayout.setBackgroundResource(R.drawable.rounded_layout_mul)
                goBack.background.setColorFilter(Color.parseColor("#EB1C24"), PorterDuff.Mode.SRC_IN)

            } else if (button31.getLocalVisibleRect(scrollBounds) || button35.getLocalVisibleRect(scrollBounds)) {
                chapterName.text = "DIVISION"
                chapterName.setTextColor(Color.parseColor("#C5BB00"))
                topLayout.setBackgroundResource(R.drawable.rounded_layout_div)
                goBack.background.setColorFilter(Color.parseColor("#FDF000"), PorterDuff.Mode.SRC_IN)

            } else {
                chapterName.text = "ADDITION"
                chapterName.setTextColor(Color.parseColor("#22B045"))
                topLayout.setBackgroundResource(R.drawable.rounded_layout)
                goBack.background.setColorFilter(Color.parseColor("#B4E41D"), PorterDuff.Mode.SRC_IN
                )
            }
            // === KONIEC NOWEJ FUNKCJONALNOŚCI ===
        }}}



