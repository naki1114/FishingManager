package com.example.fishingmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.data.HomeRecentCollection
import com.example.fishingmanager.databinding.HomeRecentcollectionItemBinding

class HomeRecentCollectionAdapter(val itemClickListener : ItemClickListener) : RecyclerView.Adapter<HomeRecentCollectionAdapter.ViewHolder>() {

    lateinit var binding : HomeRecentcollectionItemBinding
    lateinit var recentCollectionList : ArrayList<HomeRecentCollection>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = HomeRecentcollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener, parent.context)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(recentCollectionList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return recentCollectionList.size

    } // getItemCount()


    fun setItem(item : ArrayList<HomeRecentCollection>) {

        recentCollectionList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : HomeRecentcollectionItemBinding, val itemClickListener : ItemClickListener, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(recentCollection: HomeRecentCollection) {

            binding.homeRecentCollectionItem = recentCollection
            binding.itemClickListener = itemClickListener

            Glide.with(context).load(recentCollection.fishImage).into(binding.homeRecentCollectionItemFishImage)

        }

    } // ViewHolder


    class ItemClickListener(val clickListener : (recentCollection : HomeRecentCollection) -> Unit) {

        fun onClick(recentCollection: HomeRecentCollection) = clickListener(recentCollection)

    } // ItemClickListener

}