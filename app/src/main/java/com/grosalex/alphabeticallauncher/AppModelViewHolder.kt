package com.grosalex.alphabeticallauncher

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.content.pm.LauncherApps
import android.content.pm.LauncherApps.ShortcutQuery.*
import android.os.Build
import android.support.annotation.RequiresApi
import java.util.*
import android.os.Process
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ListPopupWindow.WRAP_CONTENT
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow


/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
    private val tvAppModelName = view?.findViewById<TextView>(R.id.tv_app_model_name)
    private val ivAppModelIcon = view?.findViewById<ImageView>(R.id.iv_app_model_icon)

    private var shortcutPopup: PopupWindow? = null

    fun bind(appModel: AppModel) {
        val context = itemView?.context ?: return
        tvAppModelName?.text = appModel.appName
        val packageManager = context.packageManager
        ivAppModelIcon?.setImageDrawable(appModel.getIcon(packageManager)
                ?: packageManager.defaultActivityIcon)

        itemView.setOnClickListener {
            if (!appModel.appPackageName.isEmpty())
                context.startActivity(packageManager.getLaunchIntentForPackage(appModel.appPackageName))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            itemView.setOnLongClickListener {
                showPopup(appModel, itemView)
                true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun getShortcutFromApp(context: Context, packageName: String): List<Shortcut> {
        val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        val shortcutQuery = LauncherApps.ShortcutQuery()
        shortcutQuery.setQueryFlags(FLAG_MATCH_DYNAMIC or FLAG_MATCH_MANIFEST or FLAG_MATCH_PINNED)
        shortcutQuery.setPackage(packageName)
        return try {
            launcherApps.getShortcuts(shortcutQuery, Process.myUserHandle())
                    .map { Shortcut(it.id, it.`package`, it.shortLabel.toString(), it) }
        } catch (e: SecurityException) {
            Collections.emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun showPopup(app: AppModel, itemView: View): Boolean {
        val context = itemView.context
        val shortcuts = getShortcutFromApp(context, app.appPackageName)
        if (shortcuts.isNotEmpty()) {
            val contentView = createShortcutListView(context, shortcuts)
            val locations = IntArray(2, { 0 })
            itemView.getLocationOnScreen(locations)
            shortcutPopup?.dismiss()
            shortcutPopup = PopupWindow(contentView, WRAP_CONTENT, WRAP_CONTENT, true)
            //shortcutPopup?.animationStyle = R.style.PopupAnimation
            shortcutPopup?.showAtLocation(itemView, Gravity.NO_GRAVITY,
                    locations[0] + itemView.width / 2,
                    locations[1] + itemView.height / 2)
        } else {
            /* Toast.makeText(this, getString(R.string.no_shortcut), Toast.LENGTH_SHORT).show()*/
            Log.e("showPopup", "oops")
        }
        return true
    }

    @SuppressLint("InflateParams")
    private fun createShortcutListView(context: Context, shortcuts: List<Shortcut>): View {
        val view = LayoutInflater.from(context).inflate(R.layout.popup_shortcut, null)
        val shortcutList: RecyclerView = view.findViewById(R.id.shortcutList)
        shortcutList.adapter = ShortcutListAdapter(shortcuts)
        shortcutList.layoutManager = LinearLayoutManager(context)
        return view
    }
}