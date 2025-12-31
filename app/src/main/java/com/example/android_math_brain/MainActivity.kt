package com.example.android_math_brain

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ProcessLifecycleOwner

class MainActivity : AppCompatActivity() {
    private lateinit var gameData: GameData

    override fun onCreate(savedInstanceState: Bundle?) {
        gameData = GameData.getInstance(this)
        VibrationManager.initSounds(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(this))

        if (VibrationManager.isMusicEnabled()) {
            val musicIntent = Intent(this, MusicService::class.java)
            startService(musicIntent)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val advBtn = findViewById<Button>(R.id.adventureBtn)
        val rankBtn = findViewById<Button>(R.id.rankedBtn)
        val settingsBtn = findViewById<ImageButton>(R.id.btnSettingsMain)

        updateUI()

        settingsBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            showSettingsPopup()
        }

        advBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            Intent(this, AdventureActivity::class.java).also {
                startActivity(it)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        rankBtn.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            Intent(this, RankedActivity::class.java).also {
                startActivity(it)
                finish()
            }
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

        val btnResetRanking = dialog.findViewById<Button>(R.id.btnResetRanking)
        btnResetRanking.visibility = View.VISIBLE

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

        btnResetRanking.setOnClickListener {
            VibrationManager.vibrate(this, VibrationManager.VibrationType.BUTTON_CLICK)
            gameData.setLevel(1)
            updateUI()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateUI() {
        val adventureScoreText = findViewById<TextView>(R.id.adventureScore)
        val rankedScoreText = findViewById<TextView>(R.id.rankedScore)
        adventureScoreText.text = "\uD83D\uDDFA\uFE0F" + ": " + gameData.getLevel().toString()
        rankedScoreText.text = "\uD83C\uDFC6: " + gameData.getPoints().toString()
    }

    override fun onResume() {
        super.onResume()
        if (!VibrationManager.isMusicEnabled()) {
            stopService(Intent(this, MusicService::class.java))
        }
        updateUI()
    }
}
