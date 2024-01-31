package com.example.fishingmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.databinding.ProfileSelectFishItemBinding

class ProfileSelectFishAdapter(val itemClickListener : ItemClickListener) : RecyclerView.Adapter<ProfileSelectFishAdapter.ViewHolder>() {

    lateinit var binding : ProfileSelectFishItemBinding
    lateinit var fishList : ArrayList<SelectFish>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ProfileSelectFishItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(fishList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return fishList.size

    } // getItemCount()


    fun setItem(item : ArrayList<SelectFish>) {

        fishList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : ProfileSelectFishItemBinding, val itemClickListener : ItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(selectFish : SelectFish) {

            binding.profileSelectFishItemFishImage.setImageResource(selectFish.fishImage)

        } // onBind()

    } // ViewHolder


    class ItemClickListener(val clickListener : (selectFish : SelectFish) -> Unit) {

        fun onClick(selectFish: SelectFish) = clickListener(selectFish)

    } // ItemClickListener

}