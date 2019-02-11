package com.grosalex.alphabeticallauncher.activity

import android.Manifest
import android.app.WallpaperManager
import android.content.pm.ApplicationInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.os.AsyncTask
import android.os.Handler
import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.ProgressBar
import com.google.firebase.crash.FirebaseCrash
import com.google.gson.Gson
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import com.grosalex.alphabeticallauncher.*
import com.grosalex.alphabeticallauncher.adapter.AppModelSectionMapAdapter
import com.grosalex.alphabeticallauncher.adapter.IndexAdapter
import com.grosalex.alphabeticallauncher.listener.AppListener
import com.grosalex.alphabeticallauncher.listener.IndexItemClickListener
import com.grosalex.alphabeticallauncher.model.AppModel
import com.grosalex.alphabeticallauncher.model.AppModelSectionMap


class MainActivity : AppCompatActivity(), IndexItemClickListener {

    override fun onClick(v: View, position: Int) {
        rvMain?.scrollToPosition(position)
    }

    private var fab: FloatingActionButton? = null
    private var rvMain: RecyclerView? = null
    private var rvindex: RecyclerView? = null
    private var pbvLoader: ProgressBar? = null
    private lateinit var indexLayoutManager: LinearLayoutManager
    private lateinit var indexAdapter: IndexAdapter

    private lateinit var appModelSectionMap: AppModelSectionMap

    private lateinit var appModelSectionMapAdapter: AppModelSectionMapAdapter

    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var appListenerReceiver: AppListener
    private lateinit var filter: IntentFilter

    private lateinit var recycledViewPool: RecyclerView.RecycledViewPool

    private var ivWallPaper: ImageView? = null

    private val localHandler: Handler = Handler()

    private val loadAppTaskRunnable = Runnable { LoadAppTask().execute() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pbvLoader = findViewById<ProgressBar>(R.id.progressBar)
        ivWallPaper = findViewById(R.id.iv_wallpaper)

        if (BuildConfig.DEBUG) {
            FirebaseCrash.setCrashCollectionEnabled(false);
        } else {
            FirebaseCrash.setCrashCollectionEnabled(true);
        }
        initMainRecyclerView()

        initIndexRecyclerView()

        initFabAndFav()

        ib_settings?.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        preLoadApp()
        if (appModelSectionMap.isEmpty()) LoadAppTask().execute()
        else localHandler.postDelayed(loadAppTaskRunnable, THIRTY_MINUTES)

        appListenerReceiver = AppListener(this)
        filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        this.registerReceiver(appListenerReceiver, filter)
    }

    private fun initFabAndFav() {
        fab = findViewById(R.id.fab)
    }

    private fun preLoadApp() {
        val gson = Gson()
        val pref = getSharedPreferences(APP_LIST, 0)
        val fromJson = pref.getString(APP_LIST, null) ?: return
        appModelSectionMap = gson.fromJson(fromJson, AppModelSectionMap::class.java) ?: return

        appModelSectionMapAdapter.recycledViewPool = recycledViewPool
        appModelSectionMapAdapter.items = appModelSectionMap
        appModelSectionMapAdapter.notifyDataSetChanged()
        indexAdapter.items = ArrayList(appModelSectionMap.keys)
        indexAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        requestedOrientation = if (getSharedPreferences(SETTINGS, 0).getBoolean(ALLOW_ROTATION, false)) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        val readStoragepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val requestDeletepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_DELETE_PACKAGES)

        if (readStoragepermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            ivWallPaper?.setImageDrawable(WallpaperManager.getInstance(this).drawable)
        }

        if (requestDeletepermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.REQUEST_DELETE_PACKAGES), REQUEST_DELETE_PACKAGES)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ivWallPaper?.setImageDrawable(WallpaperManager.getInstance(this).drawable)
            }
            else -> {
            }
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
    }

    private fun initIndexRecyclerView() {
        indexLayoutManager = LinearLayoutManager(this)
        indexAdapter = IndexAdapter(ArrayList(appModelSectionMap.keys), this)
        rvindex = findViewById<RecyclerView>(R.id.rv_index)
        rvindex?.layoutManager = indexLayoutManager
        rvindex?.adapter = indexAdapter
    }

    private fun getAllApp() {
        appModelSectionMap.clear()
        val allApp: List<ApplicationInfo> = packageManager.getInstalledApplications(0)

        for (applicationInfo in allApp) {
            addApp(applicationInfo)
        }
    }

    private fun saveData() {
        val gson = Gson()
        val toJson = gson.toJson(appModelSectionMap)
        val pref = getSharedPreferences(APP_LIST, 0)
        pref.edit().putString(APP_LIST, toJson).apply()
    }

    private fun addApp(applicationInfo: ApplicationInfo) {
        if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null)
            appModelSectionMap.add(AppModel(packageManager, applicationInfo))
    }

    fun packageWasRemoved() {
        LoadAppTask().execute()
    }

    fun addPackage(data: String?) {
        val applicationInfo: ApplicationInfo = packageManager?.getApplicationInfo(data, 0) ?: return

        addApp(applicationInfo)
        appModelSectionMapAdapter.notifyDataSetChanged()
        saveData()
    }

    override fun onDestroy() {
        unregisterReceiver(appListenerReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        rvMain?.scrollToPosition(0)
    }

    private inner class LoadAppTask : AsyncTask<Void, Int, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
            if (appModelSectionMap.isEmpty())
                pbvLoader?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): Void? {
            getAllApp()
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {}

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            appModelSectionMapAdapter.notifyDataSetChanged()
            indexAdapter.items = ArrayList(appModelSectionMap.keys)
            indexAdapter.notifyDataSetChanged()
            pbvLoader?.visibility = View.GONE
            saveData()
            localHandler.postDelayed(loadAppTaskRunnable, THIRTY_MINUTES)
        }
    }

    companion object {
        const val APP_LIST: String = "app_list"
        const val THIRTY_MINUTES: Long = 30 * DateUtils.MINUTE_IN_MILLIS
        const val REQUEST_READ_EXTERNAL_STORAGE: Int = 10
        const val REQUEST_DELETE_PACKAGES: Int = 11
    }
}