package com.example.android_math_brain

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object VibrationManager {
    // WYWOLANIE FUNKCJI:
    // VibrationManager.vibrate(this, VibrationManager.VibrationType.LOCKED) <- zamiast locked piszesz cos z klasy pod spodem tego enum
    // CURRENT -aktualny poziom, PASSED-poziom ktory juz przeszedles, LOCKED - zablokowany, BUTTON_CLICK <- na buttony zwykle


    enum class VibrationType {
        CURRENT,
        PASSED,
        LOCKED,
        BUTTON_CLICK,
        CORRECT,
        WRONG
    }

    fun vibrate(context: Context, vibrationType: VibrationType) {
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
                // zwykly guzik
                longArrayOf(0, 80),
                intArrayOf(0, 200)
            )

            VibrationType.CORRECT -> Pair(

                longArrayOf(0, 150),
                intArrayOf(0, 80)
            )
            VibrationType.WRONG -> Pair(

                longArrayOf(0, 200),
                intArrayOf(0, 200)
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
