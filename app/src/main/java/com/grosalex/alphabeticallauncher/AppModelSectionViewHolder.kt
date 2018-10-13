package com.grosalex.alphabeticallauncher

import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlin.collections.ArrayList

/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelSectionViewHolder(view: View?) : RecyclerView.ViewHolder(view) {

    var rvSection = view?.findViewById<RecyclerView>(R.id.rv_section)
    var tvSection = view?.findViewById<TextView>(R.id.tv_section)
    var gridLayoutManager = GridLayoutManager(view?.context,
            when (itemView.context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
                Configuration.SCREENLAYOUT_SIZE_NORMAL -> 3
                Configuration.SCREENLAYOUT_SIZE_LARGE -> 4
                Configuration.SCREENLAYOUT_SIZE_XLARGE -> 6
                else -> 3
            }
    )
    lateinit var sectionAdapter: AppModelAdapter

    fun bind(key: String, item: ArrayList<AppModel>, recycledViewPool: RecyclerView.RecycledViewPool) {
        tvSection?.text = key


        rvSection?.layoutManager = gridLayoutManager
        sectionAdapter = AppModelAdapter(item)

        rvSection?.adapter = sectionAdapter
        rvSection?.recycledViewPool = recycledViewPool

    }
}