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
    lateinit var appIcon: Drawable
    lateinit var appPackageName: String


    constructor(packageManager: PackageManager, applicationInfo: ApplicationInfo){
        appName = applicationInfo.loadLabel(packageManager).toString()
        appIcon = applicationInfo.loadIcon(packageManager)
        Log.d("APPMODEL", appName )
        Log.d("APPMODEL pckagename", "" + applicationInfo.packageName )

        appPackageName = applicationInfo.packageName
    }

}