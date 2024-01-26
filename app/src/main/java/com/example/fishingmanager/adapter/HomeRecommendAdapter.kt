package com.example.fishingmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.HomeRecommend
import com.example.fishingmanager.databinding.HomeRecommendItemBinding

class HomeRecommendAdapter : RecyclerView.Adapter<HomeRecommendAdapter.ViewHolder>() {

    lateinit var recommendList : ArrayList<HomeRecommend>
    lateinit var binding : HomeRecommendItemBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        binding = HomeRecommendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: HomeRecommendAdapter.ViewHolder, position: Int) {

        holder.onBind(recommendList[position])

    }

    override fun getItemCount(): Int {

        return recommendList.size

    }


    fun setItem(item : ArrayList<HomeRecommend>) {

        recommendList = item
        notifyDataSetChanged()

    }


    class ViewHolder(val binding : HomeRecommendItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(homeRecommend: HomeRecommend) {

            binding.homeRecommendItem = homeRecommend

            binding.homeRecommendItemFishImage.setImageResource(homeRecommend.fishImage)

        }

    }


}