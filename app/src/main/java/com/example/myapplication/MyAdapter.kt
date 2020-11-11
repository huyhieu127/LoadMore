package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*

class MyAdapter(private val data: MutableList<Item?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when(viewType){
            VIEW_TYPE_LOADING -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
                MyLoading(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
                MyItem(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyItem){
            holder.fillDataItem(data?.get(position))
        }else if (holder is MyLoading){
            holder.fillDataLoading()
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        if (data?.get(position)  == null){
            return VIEW_TYPE_LOADING
        }
        return  VIEW_TYPE_ITEM
    }

    inner class MyItem(private val view: View) : RecyclerView.ViewHolder(view) {
        fun fillDataItem(item: Item?) {
            view.txtItem.text = adapterPosition.toString()
        }
    }

    inner class MyLoading(view: View) : RecyclerView.ViewHolder(view) {
        fun fillDataLoading(){}
    }
}