package com.example.fishingmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.ConditionCombine
import com.example.fishingmanager.databinding.ConditionCombineItemBinding

class ConditionCombineAdapter : RecyclerView.Adapter<ConditionCombineAdapter.ViewHolder>() {

    val TAG = "ConditionCombineAdapter"

    lateinit var combineList : ArrayList<ConditionCombine>
    lateinit var binding : ConditionCombineItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ConditionCombineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(combineList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return combineList.size

    } // getItemCount()


    fun setItem(item : ArrayList<ConditionCombine>) {

        combineList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : ConditionCombineItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(combineList: ConditionCombine) {

            binding.combineItem = combineList
            binding.conditionCombineIndexImage.setImageResource(combineList.indexImage)
            binding.conditionCombineSkyImage.setImageResource(combineList.skyImage)

        }

    } // ViewHolder

}