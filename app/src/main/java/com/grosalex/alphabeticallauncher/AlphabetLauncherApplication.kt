package com.grosalex.alphabeticallauncher

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.grosalex.alphabeticallauncher.data.DataRepository

class AlphabetLauncherApplication : Application() {
    lateinit var analytics: FirebaseAnalytics
    val dataRepository:DataRepository = DataRepository()
    override fun onCreate() {
        super.onCreate()
        analytics = FirebaseAnalytics.getInstance(this)
    }

    companion object {
        fun get(context: Context) = context.applicationContext as AlphabetLauncherApplication
    }
}