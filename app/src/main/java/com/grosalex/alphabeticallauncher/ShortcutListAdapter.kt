package com.grosalex.alphabeticallauncher

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.LauncherApps
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.os.Process

@TargetApi(Build.VERSION_CODES.N_MR1)
class ShortcutListAdapter(private val shortcuts: List<Shortcut>)
    : RecyclerView.Adapter<ShortcutListAdapter.ViewHolder>() {

    override fun getItemCount(): Int = shortcuts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shortcut, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shortcuts[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvLabel: TextView = itemView.findViewById(R.id.tvLabel)

        fun bind(shortcut: Shortcut) {
            itemView.setOnClickListener { startShortcut(itemView.context, shortcut) }
            ivIcon.setImageDrawable(getShortcutIcon(itemView.context, shortcut.shortcutInfo))
            tvLabel.text = shortcut.label
        }
    }

    private fun loadShortcutIcon(context: Context, shortcutInfo: ShortcutInfo): Drawable? {
        return try {
            val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

            val drawable = launcherApps.getShortcutIconDrawable(shortcutInfo,
                    context.resources.displayMetrics.densityDpi)
            drawable
        } catch (e: SecurityException) {
            null
        }
    }

    fun getShortcutIcon(context: Context, shortcutInfo: ShortcutInfo) = loadShortcutIcon(context, shortcutInfo)

    fun startShortcut(context: Context, shortcut: Shortcut) {
        val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        launcherApps.startShortcut(shortcut.packageName, shortcut.id, null, null, Process.myUserHandle())
    }
}