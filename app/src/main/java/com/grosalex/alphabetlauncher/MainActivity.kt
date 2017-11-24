package com.grosalex.alphabetlauncher

import android.content.pm.ApplicationInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), IndexItemClickListener {

    override fun onClick(v: View, position: Int) {
        rvMain?.scrollToPosition(position)
    }

    private var rvMain: RecyclerView? = null
    private var rvindex: RecyclerView? = null
    private lateinit var indexLayoutManager: LinearLayoutManager
    private lateinit var indexAdapter: IndexAdapter

    private lateinit var appModelSectionMap: AppModelSectionMap

    private lateinit var appModelSectionMapAdapter: AppModelSectionMapAdapter

    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var appListenerReceiver: AppListener
    private lateinit var filter: IntentFilter

    private lateinit var recycledViewPool: RecyclerView.RecycledViewPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMainRecyclerView()

        initindexRecyclerView()

        ib_settings?.setOnClickListener {
            var intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initMainRecyclerView() {

        appModelSectionMap = AppModelSectionMap()
        rvMain = findViewById<RecyclerView>(R.id.rv_main)
        recycledViewPool = RecyclerView.RecycledViewPool()
        recycledViewPool.setMaxRecycledViews(0, 50)

        layoutManager = LinearLayoutManager(this)
        appModelSectionMapAdapter = AppModelSectionMapAdapter(appModelSectionMap, recycledViewPool)

        rvMain?.layoutManager = layoutManager

        rvMain?.adapter = appModelSectionMapAdapter

        getAllApp()
    }

    private fun initindexRecyclerView() {
        indexLayoutManager = LinearLayoutManager(this)
        indexAdapter = IndexAdapter(ArrayList(appModelSectionMap.keys), this )
        rvindex = findViewById<RecyclerView>(R.id.rv_index)
        rvindex?.layoutManager = indexLayoutManager
        rvindex?.adapter = indexAdapter


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