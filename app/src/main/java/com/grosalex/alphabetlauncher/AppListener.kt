package com.grosalex.alphabetlauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by grosalex on 23/11/2017.
 */

class AppListener(val mainActivity: MainActivity) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(this.javaClass.name, intent.toString() + intent?.extras.toString())

        for (string in intent?.extras?.keySet()!!) {
            Log.d(this.javaClass.name, string + " : " + intent?.extras[string])
        }

        when {
            Intent.ACTION_PACKAGE_ADDED.equals(intent?.action) -> {
                mainActivity.addPackage(intent?.data.toString().removePrefix("package:"))
            }
            Intent.ACTION_PACKAGE_REMOVED.equals(intent?.action) -> {
                mainActivity.packageWasRemoved()
            }
        }
    }
}
