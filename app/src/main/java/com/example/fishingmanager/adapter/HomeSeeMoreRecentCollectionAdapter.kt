package com.example.fishingmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fishingmanager.data.HomeRecentCollection
import com.example.fishingmanager.databinding.HomeSeemoreItemBinding

class HomeSeeMoreRecentCollectionAdapter : RecyclerView.Adapter<HomeSeeMoreRecentCollectionAdapter.ViewHolder>(){

    lateinit var recentCollectionList : ArrayList<HomeRecentCollection>
    lateinit var binding : HomeSeemoreItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = HomeSeemoreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, parent.context)

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


    class ViewHolder(val binding : HomeSeemoreItemBinding, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(recentCollection: HomeRecentCollection) {

            binding.homeRecentCollectionItem = recentCollection

            Glide.with(context).load(recentCollection.fishImage).into(binding.homeSeeMoreItemFishImage)
            Glide.with(context).load(recentCollection.profileImage).into(binding.homeSeeMoreItemProfileImage)

        } // onBind()

    } // ViewHolder

}