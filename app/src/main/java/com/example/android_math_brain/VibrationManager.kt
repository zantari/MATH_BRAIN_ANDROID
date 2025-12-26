package com.example.android_math_brain

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object VibrationManager {

    private var soundPool: SoundPool? = null
    private var clickSoundId: Int = 0
    private var soundReady = false

    fun initSounds(context: Context) {
        if (soundPool == null) {
            soundPool = SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                ).build()

            clickSoundId = soundPool!!.load(context, R.raw.click_sound2, 1)

            soundPool!!.setOnLoadCompleteListener { _, _, status ->
                if (status == 0) soundReady = true
            }
        }
    }

    enum class VibrationType {
        CURRENT,
        PASSED,
        LOCKED,
        BUTTON_CLICK,
        CORRECT,
        WRONG
    }

    fun vibrate(context: Context, vibrationType: VibrationType) {

        if (soundReady &&
            vibrationType != VibrationType.CORRECT &&
            vibrationType != VibrationType.WRONG) {

            soundPool?.play(clickSoundId, 1f, 1f, 1, 0, 1f)
        }


        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (!vibrator.hasVibrator()) {
            return
        }

        val (pattern, amplitudes) = when (vibrationType) {
            VibrationType.CURRENT -> Pair(
                longArrayOf(0, 150, 50, 150),
                intArrayOf(0, 150, 0, 180)
            )
            VibrationType.LOCKED -> Pair(
                longArrayOf(0, 250),
                intArrayOf(0, 200)
            )
            VibrationType.PASSED -> Pair(
                longArrayOf(0, 50, 50, 100),
                intArrayOf(0, 255, 0, 255)
            )
            VibrationType.BUTTON_CLICK -> Pair(
                longArrayOf(0, 80),
                intArrayOf(0, 200)
            )
            VibrationType.CORRECT -> Pair(
                longArrayOf(0, 80),
                intArrayOf(0, 60)
            )
            VibrationType.WRONG -> Pair(
                longArrayOf(0, 500),
                intArrayOf(0, 255)
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }
}