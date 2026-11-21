package com.example.healthappointmentmanagementsystem.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SimpleListAdapter<T>(
    private val layoutRes: Int,
    private val bind: (View, T) -> Unit
) : RecyclerView.Adapter<SimpleListAdapter.Holder>() {

    private var items: List<T> = emptyList()

    class Holder(view: View) : RecyclerView.ViewHolder(view)

    fun submit(newItems: List<T>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        bind(holder.itemView, items[position])
    }

    override fun getItemCount(): Int = items.size
}