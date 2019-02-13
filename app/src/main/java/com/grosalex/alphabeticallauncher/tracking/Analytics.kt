package com.grosalex.alphabeticallauncher.tracking

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.grosalex.alphabeticallauncher.model.Shortcut

private const val EVENT_APP_LAUNCHED = "app_launched"
private const val EVENT_INDEX_CLICKED = "index_clicked"
private const val EVENT_APP_LONG_PRESSED = "app_long_pressed"
private const val EVENT_SHORTCUT_CLICKED = "shortcut_clicked"


private const val KEY_APP_NAME = "app_name"
private const val KEY_APP_PACKAGE = "app_package"
private const val KEY_SHORTCUT_ID = "shortcut_id"
private const val KEY_SHORTCUT_PACKAGE_NAME = "shortcut_package"
private const val KEY_SHORTCUT_LABEL = "shortcut_label"

fun FirebaseAnalytics.trackApplicationStarted(appName: String, appPackageName: String) {
    logEvent(EVENT_APP_LAUNCHED, buildAppTrackBundle(appName, appPackageName))
}

fun FirebaseAnalytics.trackIndexClicked(){
    logEvent(EVENT_INDEX_CLICKED, Bundle())
}

fun FirebaseAnalytics.trackApplicationLongPressed(appName: String, appPackageName: String) {
    logEvent(EVENT_APP_LONG_PRESSED, buildAppTrackBundle(appName, appPackageName))
}

fun FirebaseAnalytics.shortcutClicked(shortcut: Shortcut) {
    val bundle = Bundle()
    bundle.putString(KEY_SHORTCUT_ID, shortcut.id)
    bundle.putString(KEY_SHORTCUT_LABEL, shortcut.label)
    bundle.putString(KEY_SHORTCUT_PACKAGE_NAME, shortcut.packageName)
    logEvent(EVENT_SHORTCUT_CLICKED, bundle)
}

private fun buildAppTrackBundle(appName: String, appPackageName: String): Bundle {
    val bundle = Bundle()
    bundle.putString(KEY_APP_NAME, appName)
    bundle.putString(KEY_APP_PACKAGE, appPackageName)
    return bundle
}