package com.grosalex.alphabeticallauncher.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.grosalex.alphabeticallauncher.R

/**
 * Created by grosalex on 23/11/2017.
 */
class IndexViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvIndex = view.findViewById<TextView>(R.id.tv_index)
    fun bind(s: String) {
        tvIndex?.text = s
    }
}