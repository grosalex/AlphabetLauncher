package com.grosalex.alphabeticallauncher

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.ArrayList

/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelAdapter(var items: ArrayList<AppModel>) : RecyclerView.Adapter<AppModelViewHolder>() {
    override fun onBindViewHolder(holder: AppModelViewHolder?, position: Int) {

        holder?.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AppModelViewHolder =
            AppModelViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.app_model_view_holder, parent, false))

}