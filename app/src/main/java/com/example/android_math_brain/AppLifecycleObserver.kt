package com.example.android_math_brain

import android.content.Context
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(private val context: Context) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        // Aplikacja wraca na pierwszy plan
        val intent = Intent(context, MusicService::class.java)
        context.startService(intent)
    }

    override fun onStop(owner: LifecycleOwner) {
        // Cała aplikacja idzie do tła (wszystkie Activity są niewidoczne)
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent)
    }
}