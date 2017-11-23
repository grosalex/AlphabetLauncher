package com.grosalex.alphabetlauncher

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import java.util.ArrayList

/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelSectionViewHolder(view: View?) : RecyclerView.ViewHolder(view) {

    var rvSection = view?.findViewById<RecyclerView>(R.id.rv_section)
    var tvSection = view?.findViewById<TextView>(R.id.tv_section)
    var gridLayoutManager = GridLayoutManager(view?.context, 3)
    lateinit var sectionAdapter: AppModelAdapter

    fun bind(key: String, item: ArrayList<AppModel>) {
        tvSection?.text = key


        rvSection?.layoutManager = gridLayoutManager
        sectionAdapter = AppModelAdapter(item)

        rvSection?.adapter = sectionAdapter

    }
}