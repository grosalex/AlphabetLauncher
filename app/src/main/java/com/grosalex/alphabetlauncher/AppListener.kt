package com.grosalex.alphabetlauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by grosalex on 23/11/2017.
 */

class AppListener(private val mainActivity: MainActivity) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
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
