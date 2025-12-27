package com.example.android_math_brain

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object VibrationManager {

    private var isVibrationEnabled = true
    private var isMusicEnabled = true
    private var soundPool: SoundPool? = null
    private var clickSoundId: Int = 0
    private var soundReady = false

    fun initSounds(context: Context) {
        if (soundPool == null) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build()

            clickSoundId = soundPool!!.load(context, R.raw.click_sound2, 1)

            soundPool!!.setOnLoadCompleteListener { _, _, status ->
                if (status == 0) soundReady = true
            }
        }
    }

    private fun playClickSound() {
        if (isMusicEnabled && soundReady) {
            soundPool?.play(clickSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    enum class VibrationType {
        BUTTON_CLICK,
        CORRECT,
        WRONG,
        LVL_UP
    }

    fun vibrate(context: Context, type: VibrationType) {
        if (isVibrationEnabled) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (vibrator.hasVibrator()) {
                val vibrationEffect = when (type) {
                    VibrationType.BUTTON_CLICK -> VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
                    VibrationType.CORRECT -> VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                    VibrationType.WRONG -> VibrationEffect.createWaveform(longArrayOf(0, 100, 100, 100), -1)
                    VibrationType.LVL_UP -> VibrationEffect.createWaveform(longArrayOf(0, 200, 200, 200), -1)
                }
                vibrator.vibrate(vibrationEffect)
            }
        }

        if (type == VibrationType.BUTTON_CLICK) {
            playClickSound()
        }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        isVibrationEnabled = enabled
    }

    fun isVibrationEnabled(): Boolean {
        return isVibrationEnabled
    }

    fun setMusicEnabled(context: Context, enabled: Boolean) {
        isMusicEnabled = enabled
        val musicIntent = Intent(context, MusicService::class.java)
        if (enabled) {
            context.startService(musicIntent)
        } else {
            context.stopService(musicIntent)
        }
    }

    fun isMusicEnabled(): Boolean {
        return isMusicEnabled
    }
}