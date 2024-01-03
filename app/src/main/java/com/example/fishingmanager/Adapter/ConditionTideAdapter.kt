package com.example.fishingmanager.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.Data.ConditionTide

class ConditionTideAdapter : RecyclerView.Adapter<ConditionTideAdapter.ViewHolder>() {

    val TAG = "ConditionTideAdapter"
    lateinit var tideList : ArrayList<ConditionTide>
    lateinit var binding : ConditionTideItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ConditionTideItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(tideList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return tideList.size

    } // getItemCount()


    fun setItem(item : ArrayList<ConditionTide>) {

        tideList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : ConditionTideItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(tideList: ConditionTide) {

            binding.tideItem = tideList

            binding.conditionTideUpDownImage.setImageResource(tideList.upDownImage)
            binding.conditionTideWaterHeightImage.setImageResource(tideList.waterHeightImage)

        }

    } // ViewHolder

}