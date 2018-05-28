package com.grosalex.alphabetlauncher

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity


/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
    private val tvAppModelName = view?.findViewById<TextView>(R.id.tv_app_model_name)
    private val ivAppModelIcon = view?.findViewById<ImageView>(R.id.iv_app_model_icon)
    fun bind(appModel: AppModel) {
        val context = itemView?.context
        tvAppModelName?.text = appModel.appName
        ivAppModelIcon?.setImageDrawable(appModel.getIcon(context?.packageManager))

        itemView?.setOnClickListener {
            if (!appModel.appPackageName.isEmpty())
                context?.startActivity(context.packageManager.getLaunchIntentForPackage(appModel.appPackageName))
        }

        itemView.setOnLongClickListener(View.OnLongClickListener {
            val alertDilog = AlertDialog.Builder(context).create()
            alertDilog.setTitle("Uninstall ?")
            alertDilog.setMessage(String.format("Do you wish to install %s app ?", appModel.appName))

            alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, context?.getString(R.string.yes), { dialogInterface, i ->
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:" + appModel.appPackageName)
                startActivity(context, intent, Bundle())
            })

            alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, context?.getString(R.string.no), { dialogInterface, i ->
                dialogInterface.dismiss()
            })

            alertDilog.show()
            true
        })
    }
}