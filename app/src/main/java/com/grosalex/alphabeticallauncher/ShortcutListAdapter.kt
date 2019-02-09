package com.grosalex.alphabeticallauncher

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.os.Process
import android.support.v4.content.ContextCompat.startActivity

@TargetApi(Build.VERSION_CODES.N_MR1)
class ShortcutListAdapter(private val shortcuts: List<Shortcut>, val onShortCutClick: onShortCutClicked)
    : RecyclerView.Adapter<ShortcutListAdapter.ViewHolder>() {

    override fun getItemCount(): Int = shortcuts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shortcut, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shortcuts[position])
    }

    // TODO extract in its own class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvLabel: TextView = itemView.findViewById(R.id.tvLabel)

        fun bind(shortcut: Shortcut) {
            itemView.setOnClickListener {
                startShortcut(itemView.context, shortcut)
                onShortCutClick.onClick()
            }
            tvLabel.text = shortcut.label

            // TODO bind the delete depending on an internal id instead of as default
            shortcut.shortcutInfo?.let {
                ivIcon.setImageDrawable(getShortcutIcon(itemView.context, it))
            } ?: run { ivIcon.setImageResource(R.drawable.ic_baseline_delete_forever_24px) }
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
        val id = shortcut.id
        if (id != null) {
            launcherApps.startShortcut(shortcut.packageName, id, null, null, Process.myUserHandle())
        } else {
            val intent = Intent(Intent.ACTION_DELETE)
            intent.data = Uri.parse("package:" + shortcut.packageName)
            startActivity(context, intent, Bundle())
        }
    }

    interface onShortCutClicked{
        fun onClick()
    }
}