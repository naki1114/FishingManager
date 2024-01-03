package com.example.fishingmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.SearchLocation
import com.example.fishingmanager.databinding.ConditionSearchLocationItemBinding

class ConditionSearchLocationAdapter(val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ConditionSearchLocationAdapter.ViewHolder>() {

    val TAG = "ConditionSearchLocationApapter"

    lateinit var locationList : ArrayList<SearchLocation>
    lateinit var binding : ConditionSearchLocationItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ConditionSearchLocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(locationList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return locationList.size

    } // getItemCount()


    fun setItem(item : ArrayList<SearchLocation>) {

        locationList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : ConditionSearchLocationItemBinding, val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(locationList: SearchLocation) {

            binding.locationList = locationList
            binding.clickListener = itemClickListener

        }

    } // ViewHolder


    class ItemClickListener(val clickListener : (locationList : SearchLocation) -> Unit) {
        fun onClick(locationList: SearchLocation) = clickListener(locationList)
    }

}