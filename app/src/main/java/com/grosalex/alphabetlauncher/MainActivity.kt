package com.grosalex.alphabetlauncher

import android.content.pm.ApplicationInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.content.Intent
import android.content.IntentFilter


class MainActivity : AppCompatActivity() {

    private var rvMain: RecyclerView? = null
    private lateinit var appModelSectionMap: AppModelSectionMap

    private lateinit var appModelSectionMapAdapter: AppModelSectionMapAdapter

    private lateinit var gridLayoutManager: LinearLayoutManager

    private lateinit var appListenerReceiver: AppListener
    private lateinit var filter: IntentFilter

    private lateinit var recycledViewPool: RecyclerView.RecycledViewPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // basics are here : http://arnab.ch/blog/2013/08/how-to-write-custom-launcher-app-in-android/
        appModelSectionMap = AppModelSectionMap()
        rvMain = findViewById<RecyclerView>(R.id.rv_main)
        recycledViewPool = RecyclerView.RecycledViewPool()
        recycledViewPool.setMaxRecycledViews(0,50)

        gridLayoutManager = LinearLayoutManager(this)
        appModelSectionMapAdapter = AppModelSectionMapAdapter(appModelSectionMap, recycledViewPool)

        rvMain?.layoutManager = gridLayoutManager

        rvMain?.adapter = appModelSectionMapAdapter

        getAllApp()


    }

    private fun getAllApp() {
        appModelSectionMap.clear()
        var allApp: List<ApplicationInfo> = packageManager.getInstalledApplications(0)

        for (applicationInfo in allApp) {
            addApp(applicationInfo)
        }
        appModelSectionMapAdapter.notifyDataSetChanged()
    }

    private fun addApp(applicationInfo: ApplicationInfo) {
        if (!applicationInfo.name.isNullOrEmpty()) {
            if (applicationInfo.packageName != null)
                appModelSectionMap.add(AppModel(packageManager, applicationInfo))
        }
    }

    fun packageWasRemoved() {
        getAllApp()
    }

    fun addPackage(data: String?) {
        val applicationInfo: ApplicationInfo = packageManager?.getApplicationInfo(data, 0) ?: return

        if (applicationInfo != null) {
            addApp(applicationInfo)
            appModelSectionMapAdapter.notifyDataSetChanged()
        }
    }


    override fun onResume() {
        super.onResume()
        appListenerReceiver = AppListener(this)
        filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        this.registerReceiver(appListenerReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(appListenerReceiver)
    }

}