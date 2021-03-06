package com.grosalex.alphabeticallauncher.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.grosalex.alphabeticallauncher.listener.IndexItemClickListener
import com.grosalex.alphabeticallauncher.R
import com.grosalex.alphabeticallauncher.viewholder.IndexViewHolder

/**
 * Created by grosalex on 23/11/2017.
 */
class IndexAdapter(var items: ArrayList<String>, private var indexItemClickListener: IndexItemClickListener) : RecyclerView.Adapter<IndexViewHolder>() {
    override fun onBindViewHolder(holder: IndexViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { indexItemClickListener.onClick(holder.itemView, position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexViewHolder =
            IndexViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.index_view_holder, parent, false))

    override fun getItemCount(): Int = items.size
}