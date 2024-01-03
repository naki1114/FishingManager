package com.example.fishingmanager.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.Data.SelectFish

class ConditionSelectFishAdapter : RecyclerView.Adapter<ConditionSelectFishAdapter.ViewHolder>() {

    val TAG = "ConditionSelectFishAdapter"

    lateinit var fishList : ArrayList<SelectFish>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ConditionSelectFishItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

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


    class ViewHolder(val binding : ConditionSelectFishItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(fishList: SelectFish) {

            binding.fishItem = fishList

        }

    } // ViewHolder

}