package com.grosalex.alphabetlauncher

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log

/**
 * Created by grosalex on 23/11/2017.
 */
class AppModel {
    lateinit var appName: String
    lateinit var appPackageName: String

    @Transient
    private var appIcon: Drawable? = null

    constructor(packageManager: PackageManager, applicationInfo: ApplicationInfo) {
        appName = applicationInfo.loadLabel(packageManager).toString()
        appPackageName = applicationInfo.packageName
        appIcon = applicationInfo.loadIcon(packageManager)
    }

    fun getIcon(packageManager: PackageManager): Drawable? = if (appIcon != null) {
        appIcon
    } else {
        appIcon = packageManager.getApplicationIcon(appPackageName)
        appIcon
    }
}