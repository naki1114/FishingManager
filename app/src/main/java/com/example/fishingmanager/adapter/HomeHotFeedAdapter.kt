package com.example.fishingmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.databinding.HomeHotfeedItemBinding

class HomeHotFeedAdapter(val itemClickListener : ItemClickListener) : RecyclerView.Adapter<HomeHotFeedAdapter.ViewHolder>() {

    lateinit var binding : HomeHotfeedItemBinding
    lateinit var feedList : ArrayList<Feed>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = HomeHotfeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener, parent.context)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(feedList[position])

    } // onBindViewHolder()

    override fun getItemCount(): Int {

        return feedList.size

    } // getItemCount()


    fun setItem(item : ArrayList<Feed>) {

        feedList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : HomeHotfeedItemBinding, val itemClickListener : ItemClickListener, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(feed : Feed) {

            binding.feed = feed
            binding.clickListener = itemClickListener

        } // onBind()

    } // ViewHolder


    class ItemClickListener(val clickListener: (feed : Feed) -> Unit) {

        fun onClick(feed : Feed) = clickListener(feed)

    } // ItemClickListener



}