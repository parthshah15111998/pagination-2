package com.example.pagination2.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pagination2.databinding.ItemRowBinding
import com.example.pagination2.modelClass.Data

class MyAdapter (val context: Context,val userList:ArrayList<Data.DataItem>):
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRowBinding):RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding=ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item=userList[position]
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}