package com.grosalex.alphabeticallauncher

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class AlphabetLauncherApplication : Application() {
    lateinit var analytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        analytics = FirebaseAnalytics.getInstance(this)
    }

    companion object {
        fun get(context: Context) = context.applicationContext as AlphabetLauncherApplication
    }
}