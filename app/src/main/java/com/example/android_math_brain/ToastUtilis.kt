package com.example.android_math_brain
import android.content.Context
import android.widget.Toast


private var lastToast: Toast? = null


//UZYCIE showToast(this, "TEXT") nie musisz nawet dlugosci ani showa pokazywac


fun showToast(kontekst: Context, wiadomosc: String) {

    lastToast?.cancel()


    lastToast = Toast.makeText(kontekst, wiadomosc, Toast.LENGTH_SHORT)


    lastToast?.show()
}