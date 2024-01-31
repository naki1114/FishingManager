package com.example.fishingmanager.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.HomeRecentCollection
import com.example.fishingmanager.databinding.HomeRecentcollectionItemBinding
import com.example.fishingmanager.network.RetrofitClient

class HomeRecentCollectionAdapter(val itemClickListener : ItemClickListener) : RecyclerView.Adapter<HomeRecentCollectionAdapter.ViewHolder>() {

    lateinit var binding : HomeRecentcollectionItemBinding
    lateinit var historyList : ArrayList<History>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = HomeRecentcollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener, parent.context)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(historyList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return historyList.size

    } // getItemCount()


    fun setItem(item : ArrayList<History>) {

        historyList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : HomeRecentcollectionItemBinding, val itemClickListener : ItemClickListener, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(history: History) {

            binding.historyItem = history
            binding.itemClickListener = itemClickListener

            Glide.with(context).load(RetrofitClient.BASE_URL + history.fishImage).into(binding.homeRecentCollectionItemFishImage)

        }

    } // ViewHolder


    class ItemClickListener(val clickListener : (history : History) -> Unit) {

        fun onClick(history: History) = clickListener(history)

    } // ItemClickListener

}