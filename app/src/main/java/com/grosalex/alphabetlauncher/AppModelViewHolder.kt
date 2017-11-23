package com.grosalex.alphabetlauncher

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
    val tvAppModelName = view?.findViewById<TextView>(R.id.tv_app_model_name)
    val ivAppModelIcon = view?.findViewById<ImageView>(R.id.iv_app_model_icon)
    fun bind(appModel: AppModel) {
        val context = itemView?.context
        tvAppModelName?.text = appModel.appName
        ivAppModelIcon?.setImageDrawable(appModel.appIcon)

        itemView?.setOnClickListener {
            if (!appModel.appPackageName.isNullOrEmpty())
                context?.startActivity(context?.packageManager.getLaunchIntentForPackage(appModel.appPackageName))
        }
    }
}