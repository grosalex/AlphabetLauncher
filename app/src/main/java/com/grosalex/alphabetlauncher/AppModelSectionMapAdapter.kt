package com.grosalex.alphabetlauncher

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelSectionMapAdapter(var items: AppModelSectionMap, var recycledViewPool: RecyclerView.RecycledViewPool) : RecyclerView.Adapter<AppModelSectionViewHolder>() {
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AppModelSectionViewHolder?, position: Int) {
        val key = items.keys.elementAt(position)
        val item = items[key] ?: return

        holder?.bind(key,item,recycledViewPool)
        holder?.setIsRecyclable(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AppModelSectionViewHolder =
            AppModelSectionViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.app_model_section_map_view_holder, parent, false))
}