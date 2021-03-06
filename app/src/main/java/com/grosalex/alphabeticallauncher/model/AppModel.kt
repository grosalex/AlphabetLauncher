package com.grosalex.alphabeticallauncher.model

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

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
        try {
            appIcon = packageManager.getApplicationIcon(appPackageName)
        }catch (e : PackageManager.NameNotFoundException){ }
        appIcon
    }
}