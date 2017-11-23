package com.grosalex.alphabetlauncher

import android.content.pm.ApplicationInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var rvMain: RecyclerView? = null
    private lateinit var appModelSectionMap: AppModelSectionMap

    private lateinit var appModelSectionMapAdapter: AppModelSectionMapAdapter

    private lateinit var gridLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // basics are here : http://arnab.ch/blog/2013/08/how-to-write-custom-launcher-app-in-android/
        appModelSectionMap = AppModelSectionMap()
        rvMain = findViewById<RecyclerView>(R.id.rv_main)

        var allApp: List<ApplicationInfo> = packageManager.getInstalledApplications(0)

/*        allApp
                .map {
                    if(it.name !=null) AppModel(it)

                }
                .forEach {
                    if (it !=null)
                    appModelSectionMap.add(it)
                }*/
        for (applicationInfo in allApp) {
            if (!applicationInfo.name.isNullOrEmpty()) {
                if( applicationInfo.packageName != null)
                    appModelSectionMap.add(AppModel(packageManager, applicationInfo))
            }
        }

        gridLayoutManager = LinearLayoutManager(this)
        appModelSectionMapAdapter = AppModelSectionMapAdapter(appModelSectionMap)

        rvMain?.layoutManager = gridLayoutManager
        rvMain?.adapter = appModelSectionMapAdapter

    }
}
