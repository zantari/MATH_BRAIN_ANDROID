package com.example.android_math_brain

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        // Tworzymy MediaPlayer raz
        mediaPlayer = MediaPlayer.create(this, R.raw.lobby_theme)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(0.6f, 0.6f)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
        return START_STICKY // Usługa zostanie odrodzona, jeśli system ją zabije
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        super.onDestroy()
    }
}