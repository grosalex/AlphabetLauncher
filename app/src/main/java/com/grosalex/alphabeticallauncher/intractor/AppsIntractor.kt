package com.grosalex.alphabeticallauncher.intractor

import android.content.Context
import android.content.pm.ApplicationInfo
import com.google.gson.Gson
import com.grosalex.alphabeticallauncher.AlphabetLauncherApplication
import com.grosalex.alphabeticallauncher.activity.MainActivity
import com.grosalex.alphabeticallauncher.contract.AppsContract
import com.grosalex.alphabeticallauncher.model.AppModel
import com.grosalex.alphabeticallauncher.model.AppModelSectionMap

/**
 * @author abruneau
 *         Created on 06/05/2019
 */
class AppsIntractor(val context: Context) : AppsContract.Intractor {

    override fun addApp(data: String?, onAppsLoaded: AppsContract.Intractor.OnAppsLoaded) {
        val packageManager = context.packageManager
        val applicationInfo: ApplicationInfo = packageManager?.getApplicationInfo(data, 0) ?: return
        if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
            AlphabetLauncherApplication.get(context).dataRepository.appModelSectionMap.add(AppModel(packageManager, applicationInfo))
            onAppsLoaded.onLoad(AlphabetLauncherApplication.get(context).dataRepository.appModelSectionMap)
        }
    }

    override suspend fun loadApps(onAppsLoaded: AppsContract.Intractor.OnAppsLoaded) {

        val loadFromMemory = context.loadFromMemory()
        loadFromMemory?.let {
            AlphabetLauncherApplication.get(context).dataRepository.appModelSectionMap = it
            onAppsLoaded.onLoad(it)
        }

        val appModelSectionMap = context.loadApps()
        context.saveData(appModelSectionMap)
        AlphabetLauncherApplication.get(context).dataRepository.appModelSectionMap = appModelSectionMap
        onAppsLoaded.onLoad(appModelSectionMap)
    }

    private fun Context.saveData(appModelSectionMap: AppModelSectionMap) {
        val gson = Gson()
        val toJson = gson.toJson(appModelSectionMap)
        val pref = getSharedPreferences(MainActivity.APP_LIST, 0)
        pref.edit().putString(MainActivity.APP_LIST, toJson).apply()
    }

    private fun Context.loadFromMemory(): AppModelSectionMap? {
        val gson = Gson()
        val pref = getSharedPreferences(MainActivity.APP_LIST, 0)
        val fromJson = pref.getString(MainActivity.APP_LIST, null) ?: return null

        return gson.fromJson(fromJson, AppModelSectionMap::class.java) ?: return null
    }

    private fun Context.loadApps(): AppModelSectionMap {

        val packageManager = this.packageManager
        val allApp: List<ApplicationInfo> = packageManager.getInstalledApplications(0)
        val appModelSectionMap = AppModelSectionMap()
        for (applicationInfo in allApp) {
            appModelSectionMap.add(AppModel(packageManager, applicationInfo))
        }
        return appModelSectionMap
    }
}